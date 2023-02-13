package me.klez.senpai.action.review

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.view.dialog.review.EditReviewDialog

class CreateReviewAction : AnAction() {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state ?: return
		val presentation = e.presentation
		presentation.isEnabled = !review.isCreated()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state ?: return

		val editReviewDialog = EditReviewDialog()
		if (editReviewDialog.showAndGet()) {
			review.create(project, editReviewDialog.getDetails())
		}
	}
}
