package me.klez.senpai.action.review

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import me.klez.senpai.persistence.ReviewPersistence

class CloseReviewAction : AnAction() {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state ?: return
		val presentation = e.presentation
		presentation.isEnabled = review.isOpened()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state ?: return
		review.close()
	}
}
