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

package me.klez.senpai.action.report

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Messages
import me.klez.senpai.persistence.ReviewPersistence
import java.awt.Desktop
import java.io.File

class GenerateReportAction : AnAction() {
	override fun update(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val presentation = e.presentation
		presentation.isEnabled = review.isNotEmpty()
	}

	override fun actionPerformed(e: AnActionEvent) {
		val project = e.project ?: return
		val review = project.service<ReviewPersistence>().state
		val outputPath = "${review.projectBasePath}/senpai/reports"
		review.generateReport(outputPath)
		val reportPath = "$outputPath/index.html"
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
			val toURI = File(reportPath).toURI()
			Desktop.getDesktop().browse(toURI)
		} else {
			val reportRelativePath = reportPath.replace(review.projectBasePath!!, "")
			Messages.showInfoMessage(
				"Your report has been successfully created.\nYou can find it here:\n$reportRelativePath",
				"Generate Report"
			)
		}
	}
}
