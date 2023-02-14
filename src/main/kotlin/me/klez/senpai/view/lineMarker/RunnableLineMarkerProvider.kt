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

package me.klez.senpai.view.lineMarker

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.components.service
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence

class RunnableLineMarkerProvider : LineMarkerProvider {
	override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
		return null
	}

	override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
		if (elements.isEmpty()) return
		val project = elements[0].project
		val reviewComments = project.service<ReviewPersistence>().state.sortedFilesComments()
		val projectBasePath = project.basePath ?: return
		val containingFile = elements[0].containingFile
		val containingFileText = containingFile.text
		val virtualFilePath = containingFile.virtualFile.path
		val currentFileRelativePath = virtualFilePath.replace(projectBasePath, "")
		val reviewCommentsForCurrentFile = reviewComments[currentFileRelativePath] ?: return
		val reviewCommentsForCurrentFileByLine = mutableMapOf<Int, MutableList<ReviewFileComment>>()
		reviewCommentsForCurrentFile.forEach(fun(reviewComment) {
			val lineReviewComments =
				reviewCommentsForCurrentFileByLine[reviewComment.highlightStartingLine] ?: mutableListOf()
			lineReviewComments.add(reviewComment)
			reviewCommentsForCurrentFileByLine[reviewComment.highlightStartingLine] = lineReviewComments
		})
		elements.forEach(fun(element) {
			val elementLine = StringUtil.offsetToLineNumber(containingFileText, element.textRange.startOffset) + 1
			val lineReviewComments = reviewCommentsForCurrentFileByLine[elementLine]
			lineReviewComments?.forEach(fun(reviewComment) {
				result.add(CommentLineMarkerFactory().create(element, reviewComment))
				reviewCommentsForCurrentFileByLine.remove(elementLine)
			})
		})
		if (reviewCommentsForCurrentFileByLine.isNotEmpty()) {
			reviewCommentsForCurrentFileByLine.values.flatten().forEach(fun(reviewComment) {
				val firstElement = elements.first()
				result.add(CommentLineMarkerFactory().create(firstElement, reviewComment))
			})
		}
	}
}
