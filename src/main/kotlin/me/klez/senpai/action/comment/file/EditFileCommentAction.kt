/*
 * Project senpai, 2023-02-14T16:36+0100
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
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.components.service
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.view.dialog.comment.EditCommentDialog

class EditFileCommentAction(private val fileComment: ReviewFileComment) :
	AnAction("Edit comment", "", AllIcons.Actions.Edit) {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val presentation = e.presentation
		presentation.isEnabled = review.isOpened()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val containingFile = e.getData(LangDataKeys.PSI_FILE)?.originalFile ?: return
		val updatedComment = fileComment.clone()
		val editCommentDialog =
			EditCommentDialog("Edit File Comment", updatedComment)
		if (editCommentDialog.showAndGet()) {
			updatedComment.details = editCommentDialog.getDetails()
			review.replaceFileComment(fileComment, updatedComment)
			DaemonCodeAnalyzer.getInstance(project).restart(containingFile)
		}
	}
}
