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

import com.intellij.execution.lineMarker.LineMarkerActionWrapper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.util.Function
import me.klez.senpai.action.comment.file.EditFileCommentAction
import me.klez.senpai.action.comment.file.RemoveFileCommentAction
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.persistence.ReviewPersistence
import me.klez.senpai.util.prepend
import me.klez.senpai.util.wrap

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
			val review = project.service<ReviewPersistence>().state
			if (!review.isOpened()) {
				removeAll()
			} else {
				if (childActionsOrStubs.isEmpty()) addAllActions()
			}
		}
	}
}
