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

package me.klez.senpai.report

import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.ReviewDetails
import me.klez.senpai.model.review.ReviewStatus
import me.klez.senpai.model.review.comment.CommentTag
import me.klez.senpai.model.review.comment.ReviewCommentDetails
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.model.review.comment.ReviewGeneralComment
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Paths

class ReportGeneratorTest {
	private lateinit var review: Review

	@BeforeEach
	fun setUp() {
		val outputPath = Paths.get("build").toFile().absolutePath
		review = Review().apply {
			status = ReviewStatus.OPENED
			projectBasePath = outputPath
			details = ReviewDetails("Alessandro Accardo", 1, "My first code review EVER")
			addGeneralComment(ReviewGeneralComment(ReviewCommentDetails().apply {
				label = "[C01] - Inappropriate Information"
				description =
					"It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Change histories, for example, just clutter up source files with volumes of historical and uninteresting text. In general, meta-data such as authors, last-modified-date, SPR number, and so on should not appear in comments. Comments should be reserved for technical notes about the code and design."
				tags += CommentTag.CLEAN_CODE
			}))
			addFileComment(ReviewFileComment().apply {
				filePath = "/pom.xml"
				codeSnippet = """<?xml version="1.0" encoding="utf-8" ?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <artifactId>test-senpai</artifactId>

</project>"""
				details = ReviewCommentDetails().apply {
					label = "[C01] - Inappropriate Information"
					description =
						"It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Change histories, for example, just clutter up source files with volumes of historical and uninteresting text. In general, meta-data such as authors, last-modified-date, SPR number, and so on should not appear in comments. Comments should be reserved for technical notes about the code and design."
					tags += CommentTag.CLEAN_CODE
				}
				startingLine = 1
				highlightStartingLine = 4
				highlightEndingLine = 4
			})
		}
	}

	@Test
	fun generateTL() {
		val generator = ReportGenerator()
		generator.generateTL(review, "${review.projectBasePath}/senpai-test/reportsTL")
	}

	@Test
	fun generate() {
		val generator = ReportGenerator()
		generator.generate(review, "${review.projectBasePath}/senpai-test/reports")
	}
}
