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

package me.klez.senpai.action.review

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages
import me.klez.senpai.persistence.ReviewPersistence

class DeleteReviewAction : AnAction() {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val presentation = e.presentation
		presentation.isEnabled = review.isCreated()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val evaluationName = review.details.evaluationName!!
		val dialog = Messages.showInputDialog(
			"Are you sure you want to delete this review?\nIf so, write the evaluation name ($evaluationName) to continue.",
			"Delete Review",
			Messages.getWarningIcon(),
			"",
			DisruptiveInputValidator(evaluationName)
		)
		if (dialog != null) {
			review.delete()
			DaemonCodeAnalyzer.getInstance(project).restart()
		}
	}

	private class DisruptiveInputValidator(private val disruptiveValue: String) : InputValidator {
		override fun checkInput(inputString: String?): Boolean {
			return isValid(inputString)
		}

		override fun canClose(inputString: String?): Boolean {
			return isValid(inputString)
		}

		private fun isValid(value: String?): Boolean {
			return value != null && value == disruptiveValue
		}
	}
}
