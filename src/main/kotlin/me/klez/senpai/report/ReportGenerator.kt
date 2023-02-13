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
        val indexContents: String = engine.getTemplate("index.html", mapOf(Pair("review",  review)))
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
