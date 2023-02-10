package me.klez.senpai.action.comment.general

import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.view.dialog.comment.ManageGeneralCommentsDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class ManageGeneralCommentsAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        val review = project.service<ReviewPersistence>().state ?: return
        val presentation = e.presentation
        presentation.isEnabled = review.hasGeneralComments() && review.isOpened()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val review = project.service<ReviewPersistence>().state ?: return

        val manageGeneralCommentsDialog = ManageGeneralCommentsDialog(review)
        if (manageGeneralCommentsDialog.showAndGet()) {
            // TODO
        }
    }
}
