package me.klez.senpai.action.comment.general

import me.klez.senpai.model.review.comment.ReviewGeneralComment
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.view.dialog.comment.EditCommentDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class AddGeneralCommentAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val project = e.project ?: return
        val review = project.service<ReviewPersistence>().state ?: return
        val presentation = e.presentation
        presentation.isEnabled = review.isOpened()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val review = project.service<ReviewPersistence>().state ?: return

        val editCommentDialog = EditCommentDialog("Add General Comment")
        if (editCommentDialog.showAndGet()) {
            val generalComment = ReviewGeneralComment(editCommentDialog.getDetails())
            review.addGeneralComment(generalComment)
        }
    }
}
