/*
 * Project senpai
 *
 * Copyright 2023-2023 Alessandro 'kLeZ' Accardo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.klez.senpai.action.comment.file

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.util.getRelativeFilePath
import me.klez.senpai.view.dialog.comment.EditCommentDialog
import java.lang.Integer.max
import java.lang.Integer.min

class AddFileCommentAction : AnAction() {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val presentation = e.presentation
		presentation.isEnabled = review.isOpened()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val caret = e.getData(LangDataKeys.CARET) ?: return
		val editor = e.getData(LangDataKeys.EDITOR) ?: return
		val document = editor.document
		val virtualFile = e.getData(LangDataKeys.VIRTUAL_FILE) ?: return
		val containingFile = e.getData(LangDataKeys.PSI_FILE)?.originalFile ?: return
		val review = project.service<ReviewPersistence>().state

		val selectionStartingLine = editor.visualToLogicalPosition(caret.selectionStartPosition).line + 1
		val selectionEndingLine = editor.visualToLogicalPosition(caret.selectionEndPosition).line + 1
		val selectionText = getTextForLines(document, selectionStartingLine, selectionEndingLine)
		if (selectionText.isBlank()) return
		val chunkLineRange = IntRange(selectionStartingLine, selectionEndingLine)
		val (startingLine, codeSnippet) = getCodeSnippet(chunkLineRange, document)
		if (codeSnippet.isBlank()) return
		val editCommentDialog = EditCommentDialog("Add File Comment")
		if (editCommentDialog.showAndGet()) {
			val fileComment =
				ReviewFileComment(
					getRelativeFilePath(project, virtualFile),
					startingLine,
					selectionStartingLine,
					selectionEndingLine,
					codeSnippet,
					editCommentDialog.getDetails()
				)
			persistFileComment(review, fileComment, project, containingFile)
		}
	}

	private fun getCodeSnippet(
		chunkLineRange: IntRange,
		document: Document
	): Pair<Int, String> {
		val selectionStartLine = max(chunkLineRange.first - 10, 1)
		val selectionEndLine = min(chunkLineRange.last + 10, document.lineCount)
		val codeSnippet = getTextForLines(document, selectionStartLine, selectionEndLine)
		return normalizeWhiteSpaces(selectionStartLine, codeSnippet)
	}

	private fun getTextForLines(
		document: Document,
		startLine: Int,
		endLine: Int
	): String {
		val startOffset = document.getLineStartOffset(startLine - 1)
		val endOffset = document.getLineEndOffset(endLine - 1)
		val textRange = TextRange.from(startOffset, endOffset - startOffset)
		return document.getText(textRange)
	}

	private fun normalizeWhiteSpaces(startingLine: Int, codeSnippet: String): Pair<Int, String> {
		val (_startingLine, _codeSnippet) = removeHeadingEmptyLines(codeSnippet, startingLine)
		return removeIndent(_startingLine, _codeSnippet)
	}

	private fun removeHeadingEmptyLines(
		codeSnippet: String,
		startingLine: Int
	): Pair<Int, String> {
		val lines = codeSnippet.split("\n")
		val firstNotEmptyLineIndex = lines.indexOfFirst(fun(line) = line.isNotEmpty())
		val normalizedLines = lines.slice(IntRange(firstNotEmptyLineIndex, lines.lastIndex))
		return Pair(startingLine + firstNotEmptyLineIndex, normalizedLines.joinToString("\n"))
	}

	private fun removeIndent(startingLine: Int, codeSnippet: String): Pair<Int, String> {
		val lines = codeSnippet.split("\n")
		var commonPrefix = lines[0]
		lines.slice(IntRange(1, lines.lastIndex)).forEach(fun(line) {
			if (line.isNotEmpty()) commonPrefix = commonPrefix.commonPrefixWith(line)
		})
		return Pair(
			startingLine,
			lines.joinToString("\n", transform = fun(line): String { return line.removePrefix(commonPrefix) })
		)
	}

	private fun persistFileComment(
		review: Review,
		fileComment: ReviewFileComment,
		project: Project,
		containingFile: PsiFile
	) {
		review.addFileComment(fileComment)
		DaemonCodeAnalyzer.getInstance(project).restart(containingFile)
	}
}
