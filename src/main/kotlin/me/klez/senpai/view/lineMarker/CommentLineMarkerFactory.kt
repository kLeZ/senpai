package me.klez.senpai.view.lineMarker

import me.klez.senpai.action.comment.file.EditFileCommentAction
import me.klez.senpai.action.comment.file.RemoveFileCommentAction
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.util.prepend
import me.klez.senpai.util.wrap
import com.intellij.execution.lineMarker.LineMarkerActionWrapper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.util.Function

class CommentLineMarkerFactory {
    fun create(element: PsiElement, fileComment: ReviewFileComment): CommentLineMarker {
        return CommentLineMarker(
            element,
            createCommentLineMarkerTooltipProvider(fileComment),
            CommentLineMarkerActionGroup(element, fileComment)
        )
    }

    private fun createCommentLineMarkerTooltipProvider(fileComment: ReviewFileComment): Function<PsiElement, String> {
        return Function {
            val tooltip = StringBuilder()
            tooltip.append("Code Review Comment")
            val reviewCommentDetails = fileComment.details
            tooltip.append("\n\nLabel:\n")
            tooltip.append(reviewCommentDetails.label)
            val description = reviewCommentDetails.description
            if (description.isNotBlank()) {
                tooltip.append("\n\nDescription:\n")
                tooltip.append(prepend(wrap(description, 60), "\t"))
            }
            val tags = reviewCommentDetails.tags
            if (tags.isNotEmpty()) {
                tooltip.append("\n\nTags:\n")
                tooltip.append("\t" + tags.joinToString(", "))
            }
            if (tooltip.isEmpty()) "" else tooltip.toString()
        }
    }

    private class CommentLineMarkerActionGroup(
        private val element: PsiElement,
        private val fileComment: ReviewFileComment
    ) :
        DefaultActionGroup() {
        init {
            addAllActions()
        }

        private fun addAllActions() {
            add(
                LineMarkerActionWrapper(
                    element,
                    EditFileCommentAction(fileComment)
                )
            )
            add(Separator())
            add(
                LineMarkerActionWrapper(
                    element,
                    RemoveFileCommentAction(fileComment)
                )
            )
            add(Separator())
        }

        override fun update(e: AnActionEvent) {
            val project = e.project ?: return
            val review = project.service<ReviewPersistence>().state ?: return
            if (!review.isOpened()) {
                removeAll()
            } else {
                if (childActionsOrStubs.isEmpty()) addAllActions()
            }
        }
    }
}
