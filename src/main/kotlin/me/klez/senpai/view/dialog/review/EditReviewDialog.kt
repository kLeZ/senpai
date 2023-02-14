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

package me.klez.senpai.view.dialog.review

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import me.klez.senpai.model.review.ReviewDetails
import me.klez.senpai.view.dialog.Label
import me.klez.senpai.view.dialog.Panel
import me.klez.senpai.view.dialog.validation.ValidationError
import me.klez.senpai.view.dialog.validation.invalidField
import me.klez.senpai.view.dialog.validation.missingField
import java.lang.Integer.parseInt
import javax.swing.JComponent

class EditReviewDialog : DialogWrapper(true) {
	private val reviewerField = JBTextField()
	private val teamNumberField = JBTextField()
	private val evaluationNameField = JBTextField()

	init {
		init()
		title = "Create Review"
	}

	override fun createCenterPanel(): JComponent {
		val panel = Panel(300, 200, 4)
		val gb = panel.gridBag
		panel.add(Label("Reviewer"), gb.nextLine().next())
		panel.add(reviewerField, gb.nextLine().coverLine())
		panel.add(Label("Team Number"), gb.nextLine().next())
		panel.add(teamNumberField, gb.nextLine().coverLine())
		panel.add(Label("Evaluation Name"), gb.nextLine().next())
		panel.add(evaluationNameField, gb.nextLine().coverLine())
		return panel
	}

	override fun doOKAction() {
		try {
			validateInputs()
		} catch (e: ValidationError) {
			return
		}
		close(0)
	}

	private fun validateInputs() {
		checkMissingReviewer()
		checkMissingTeamNumber()
		checkMissingEvaluationName()
		checkValidTeamNumber()
	}

	private fun checkMissingReviewer() {
		if (reviewerField.text.isBlank()) {
			missingField("Reviewer")
			throw ValidationError()
		}
	}

	private fun checkMissingTeamNumber() {
		if (teamNumberField.text.isBlank()) {
			missingField("Team Number")
			throw ValidationError()
		}
	}

	private fun checkMissingEvaluationName() {
		if (evaluationNameField.text.isBlank()) {
			missingField("Evaluation Name")
			throw ValidationError()
		}
	}

	private fun checkValidTeamNumber() {
		try {
			parseInt(teamNumberField.text)
		} catch (e: NumberFormatException) {
			invalidField("Team Number", "Team Number must be an integer")
			throw ValidationError()
		}
	}

	fun getDetails(): ReviewDetails {
		return ReviewDetails(
			getReviewer(),
			getTeamNumber(),
			getEvaluationName()
		)
	}

	private fun getReviewer(): String? {
		return reviewerField.text
	}

	private fun getTeamNumber(): Int {
		return teamNumberField.text.toInt()
	}

	private fun getEvaluationName(): String? {
		return evaluationNameField.text
	}
}
