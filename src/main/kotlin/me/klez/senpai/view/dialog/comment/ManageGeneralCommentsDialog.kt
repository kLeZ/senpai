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

package me.klez.senpai.view.dialog.comment

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.ui.HideableTitledPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.comment.ReviewGeneralComment
import me.klez.senpai.view.dialog.Label
import me.klez.senpai.view.dialog.Panel
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.Action
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
import javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED

class ManageGeneralCommentsDialog(private val review: Review) :
	DialogWrapper(true) {
	init {
		init()
		title = "Manage General Comments"
		setSize(700, 400)
	}

	override fun createActions(): Array<Action> {
		return arrayOf(myOKAction)
	}

	override fun createCenterPanel(): JComponent {
		val panel = Panel()
		val gb = panel.gridBag
		review.sortedGeneralComments().forEach(fun(comment) {
			val titledPanel = HideableTitledPanel(comment.details.label, null, false)
			val subPanel = generateGeneralCommentComponent(review, comment, titledPanel, panel)
			titledPanel.setContentComponent(subPanel)
			panel.add(titledPanel, gb.nextLine().next())
		})
		val group = JBScrollPane(panel, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER)
		group.minimumSize = Dimension(700, 400)
		val mainPanel = Panel()
		val mainGb = mainPanel.gridBag
		mainPanel.add(Label("General Comments"), mainGb.nextLine().next())
		mainPanel.add(group, mainGb.nextLine().next())
		mainPanel.minimumSize = Dimension(700, 400)
		mainPanel.maximumSize = Dimension(700, 400)
		return mainPanel
	}

	private class EditCommentActionListener(
		private val review: Review,
		private val comment: ReviewGeneralComment,
		private val titledPanel: HideableTitledPanel,
		private val panel: Panel
	) :
		ActionListener {
		override fun actionPerformed(p0: ActionEvent?) {
			val updatedComment = comment.clone()
			val editCommentDialog =
				EditCommentDialog("Edit General Comment", updatedComment)
			if (editCommentDialog.showAndGet()) {
				updatedComment.details = editCommentDialog.getDetails()
				review.replaceGeneralComment(comment, updatedComment)

				val parent = titledPanel.parent
				val newTitledPanel = HideableTitledPanel(updatedComment.details.label, null, true)
				newTitledPanel.setContentComponent(
					generateGeneralCommentComponent(
						review,
						updatedComment,
						newTitledPanel,
						panel
					)
				)
				newTitledPanel.invalidate()
				parent.remove(titledPanel)
				parent.add(newTitledPanel, panel.gridBag.nextLine().next())
				parent.invalidate()
				parent.repaint()
				parent.parent.invalidate()
				parent.parent.repaint()
			}
		}
	}

	private class RemoveCommentActionListener(
		private val review: Review,
		private val comment: ReviewGeneralComment,
		private val titledPanel: HideableTitledPanel
	) : ActionListener {
		override fun actionPerformed(p0: ActionEvent?) {
			if (Messages.showOkCancelDialog(
					"Are you sure you want to delete this comment?",
					"Remove Comment",
					"Ok",
					"Cancel",
					Messages.getWarningIcon()
				) == Messages.OK
			) {
				review.removeGeneralComment(comment)
				val parent = titledPanel.parent
				parent.remove(titledPanel)
				parent.invalidate()
				parent.repaint()
			}
		}
	}

	companion object {
		private fun generateGeneralCommentComponent(
			review: Review,
			comment: ReviewGeneralComment,
			titledPanel: HideableTitledPanel,
			panel: Panel
		): Panel {
			val subPanel = Panel(3)
			val subPanelGb = subPanel.gridBag
			val descriptionField = JBTextArea(comment.details.description)
			descriptionField.isEditable = false
			descriptionField.lineWrap = true
			subPanel.add(descriptionField, subPanelGb.nextLine().next().coverLine())
			subPanelGb.nextLine().next()
			val editButton = JButton("Edit")
			editButton.addActionListener(EditCommentActionListener(review, comment, titledPanel, panel))
			subPanel.add(editButton, subPanelGb.next())
			val removeButton = JButton("Remove")
			removeButton.addActionListener(RemoveCommentActionListener(review, comment, titledPanel))
			subPanel.add(removeButton, subPanelGb.next())
			return subPanel
		}
	}
}
