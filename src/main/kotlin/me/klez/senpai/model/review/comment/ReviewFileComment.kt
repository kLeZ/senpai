package me.klez.senpai.model.review.comment

import me.klez.senpai.report.file.FileExtension
import me.klez.senpai.report.file.extractPathStructure

class ReviewFileComment() : ReviewComment() {
	var filePath = ""
	var startingLine = 0
	var highlightStartingLine = 0
	var highlightEndingLine = 0
	var codeSnippet = ""
	val brush: String
		get() {
			return when {
				filePath.isNotBlank() -> FileExtension.fromExtension(extractPathStructure(filePath).extension).brush
				else -> ""
			}
		}

	constructor(
		_filePath: String,
		_startingLine: Int,
		_highlightStartingLine: Int,
		_highlightEndingLine: Int,
		_codeSnippet: String,
		_details: ReviewCommentDetails
	) : this() {
		filePath = _filePath
		startingLine = _startingLine
		highlightStartingLine = _highlightStartingLine
		highlightEndingLine = _highlightEndingLine
		codeSnippet = _codeSnippet
		details = _details
	}

	fun clone(): ReviewFileComment {
		return ReviewFileComment(
			filePath,
			startingLine,
			highlightStartingLine,
			highlightEndingLine,
			codeSnippet,
			details.clone()
		)
	}
}
