package me.klez.senpai.action.comment.file

import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence
import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages

class RemoveFileCommentAction(private val fileComment: ReviewFileComment) :
    AnAction("Remove comment", "", AllIcons.General.Remove) {
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
        if (Messages.showOkCancelDialog(
                project,
                "Are you sure you want to delete this comment?",
                "Remove Comment",
                "Ok",
                "Cancel",
                Messages.getWarningIcon()
            ) == Messages.OK
        ) {
            review.removeFileComment(fileComment)
            DaemonCodeAnalyzer.getInstance(project).restart(containingFile)
        }
    }
}
