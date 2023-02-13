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
		val review = project.service<ReviewPersistence>().state ?: return
		val presentation = e.presentation
		presentation.isEnabled = review.isOpened()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state ?: return
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
