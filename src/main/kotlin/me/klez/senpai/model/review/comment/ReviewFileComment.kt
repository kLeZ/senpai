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
