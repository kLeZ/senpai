package me.klez.senpai.report

import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.ReviewDetails
import me.klez.senpai.model.review.ReviewStatus
import me.klez.senpai.model.review.comment.CommentTag
import me.klez.senpai.model.review.comment.ReviewCommentDetails
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.model.review.comment.ReviewGeneralComment
import org.junit.jupiter.api.Assertions.*
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
				description = "It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Change histories, for example, just clutter up source files with volumes of historical and uninteresting text. In general, meta-data such as authors, last-modified-date, SPR number, and so on should not appear in comments. Comments should be reserved for technical notes about the code and design."
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
					description = "It is inappropriate for a comment to hold information better held in a different kind of system such as your source code control system, your issue tracking system, or any other record-keeping system. Change histories, for example, just clutter up source files with volumes of historical and uninteresting text. In general, meta-data such as authors, last-modified-date, SPR number, and so on should not appear in comments. Comments should be reserved for technical notes about the code and design."
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
