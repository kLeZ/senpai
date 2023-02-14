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

package me.klez.senpai.report

import me.klez.senpai.model.review.Review
import me.klez.senpai.report.file.copyResourcesRecursively
import me.klez.senpai.report.file.createDirectories
import me.klez.senpai.report.file.deleteDirectory
import me.klez.senpai.report.file.replaceInFile
import me.klez.senpai.report.html.GenericTemplateEngine
import me.klez.senpai.report.html.HtmlBuffer
import me.klez.senpai.report.html.node.*
import org.thymeleaf.templatemode.TemplateMode
import java.io.File

class ReportGenerator {
	fun generateTL(review: Review, outputPath: String) {
		setUpOutputDirectory(outputPath)
		prepareAssets(outputPath)
		val engine = GenericTemplateEngine(TemplateMode.HTML)
		val indexContents: String = engine.getTemplate("index.html", mapOf(Pair("review", review)))
		File("$outputPath/index.html").writeText(indexContents)
	}

	fun generate(review: Review, outputPath: String) {
		setUpOutputDirectory(outputPath)
		prepareAssets(outputPath)
		val htmlBuffer = HtmlBuffer()
		Root()
			.addChild(Headers())
			.addChild(
				Body()
					.addChild(CommentsPanel(review))
					.addChild(SnippetsPanel(review))
			).output(htmlBuffer)
		htmlBuffer.saveToFile("$outputPath/index.html")
	}

	private fun setUpOutputDirectory(path: String) {
		deleteDirectory(path)
		createDirectories(path)
	}

	private fun prepareAssets(path: String) {
		copyResourcesRecursively(
			this.javaClass.classLoader,
			"assets",
			path
		)
		replaceInFile("$path/assets/css/fonts.css", "<projectBasePath>", path)
	}
}
