package me.klez.senpai.report.html.node

import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.comment.ReviewComment
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.model.review.comment.ReviewGeneralComment
import me.klez.senpai.report.html.HtmlBuffer
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME
import java.util.*

class CommentsPanel(private val review: Review) : HtmlNode() {
    override fun openTag(buffer: HtmlBuffer) {
        buffer.append("<div class='comments-panel'>")
        buffer.increaseIndent()
        appendCommentsPanelHeader(buffer)
        appendGeneralComments(buffer)
        appendFilesComments(buffer)
        appendCopyright(buffer)
    }

    private fun appendCommentsPanelHeader(buffer: HtmlBuffer) {
        buffer.append("<div class='comments-panel-header'>")
        buffer.increaseIndent()
        appendCommentsPanelHeaderTitle(buffer)
        appendCommentsPanelHeaderDetails(buffer)
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentsPanelHeaderTitle(buffer: HtmlBuffer) {
        buffer.append("<div class='comments-panel-header-title'>")
        buffer.increaseIndent()
        buffer.append(review.details.evaluationName!!.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentsPanelHeaderDetails(buffer: HtmlBuffer) {
        buffer.append("<div class='comments-panel-header-details'>")
        buffer.increaseIndent()
        buffer.append("<div class='comments-panel-header-details-reviewer'>${review.details.reviewer!!}</div>")
        val formattedTeamNumber = review.details.teamNumber!!.toString().padStart(2, '0')
        buffer.append("<div class='comments-panel-header-details-team-number'>Team #$formattedTeamNumber</div>")
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendGeneralComments(buffer: HtmlBuffer) {
        if (!review.hasGeneralComments()) return
        buffer.append("<div class='general-comments'>")
        buffer.increaseIndent()
        buffer.append("<div class='general-comments-title'>General Comments</div>")
        buffer.append("<div class='general-comments-content'>")
        buffer.increaseIndent()
        review.sortedGeneralComments().forEachIndexed(fun(index, generalComment) {
            appendGeneralComment(buffer, index, generalComment)
        })
        buffer.decreaseIndent()
        buffer.append("</div>")
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendGeneralComment(buffer: HtmlBuffer, index: Int, generalComment: ReviewGeneralComment) {
        appendComment(buffer, index, generalComment, "general-comment")
    }

    private fun appendFilesComments(buffer: HtmlBuffer) {
        if (!review.hasFileComments()) return
        buffer.append("<div class='files-comments'>")
        buffer.increaseIndent()
        buffer.append("<div class='files-comments-title'>Specific Comments</div>")
        buffer.append("<div class='files-comments-content'>")
        buffer.increaseIndent()
        review.sortedFilesComments().forEach(fun(fileName, fileComments) {
            appendFileComments(buffer, fileName, fileComments)
        })
        buffer.decreaseIndent()
        buffer.append("</div>")
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendFileComments(
        buffer: HtmlBuffer,
        fileName: String,
        fileComments: MutableList<ReviewFileComment>
    ) {
        buffer.append("<details data-key='$fileName' class='file-comments'>")
        buffer.increaseIndent()
        buffer.append("<summary class='file-comments-summary'>$fileName</summary>")
        buffer.append("<div class='file-comments-details'>")
        buffer.increaseIndent()
        fileComments.sortBy { it.startingLine }
        fileComments.forEachIndexed(fun(index, fileComment) {
            appendFileComment(buffer, index, fileComment)
        })
        buffer.decreaseIndent()
        buffer.append("</div>")
        buffer.decreaseIndent()
        buffer.append("</details>")
    }

    private fun appendFileComment(
        buffer: HtmlBuffer,
        index: Int,
        fileComment: ReviewFileComment
    ) {
        appendComment(buffer, index, fileComment, "file-comment")
    }

    private fun appendComment(buffer: HtmlBuffer, index: Int, comment: ReviewComment, cssClass: String) {
        buffer.append("<div data-key='${comment.hashCode()}-$index' class='senpai-comment $cssClass'>")
        buffer.increaseIndent()
        appendCommentHeader(buffer, comment)
        appendCommentBody(buffer, comment)
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentHeader(buffer: HtmlBuffer, comment: ReviewComment) {
        buffer.append("<div class='comment-header'>")
        buffer.increaseIndent()
        appendCommentHeaderLogo(buffer)
        appendCommentHeaderLabel(buffer, comment)
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentHeaderLogo(buffer: HtmlBuffer) {
        buffer.append("<img class='comment-header-logo' src='assets/img/lotus.png' type='image/png'/>")
    }

    private fun appendCommentHeaderLabel(
        buffer: HtmlBuffer,
        comment: ReviewComment
    ) {
        buffer.append("<div class='comment-header-label'>")
        buffer.increaseIndent()
        buffer.append(getCommentLabel(comment))
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun getCommentLabel(comment: ReviewComment): String {
        var contentHeader = ""
        if (comment is ReviewFileComment) {
            contentHeader += "Line ${comment.highlightStartingLine} - "
        }
        contentHeader += comment.details.label
        return contentHeader
    }

    private fun appendCommentBody(buffer: HtmlBuffer, comment: ReviewComment) {
        buffer.append("<div class='comment-body'>")
        buffer.increaseIndent()
        appendCommentBodyContent(buffer, comment)
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentBodyContent(
        buffer: HtmlBuffer,
        comment: ReviewComment
    ) {
        if (comment.empty) return
        buffer.append("<div class='comment-body-content'>")
        buffer.increaseIndent()
        appendCommentBodyContentTags(buffer, comment)
        appendCommentBodyContentDescription(buffer, comment)
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentBodyContentTags(
        buffer: HtmlBuffer,
        comment: ReviewComment
    ) {
        val tags = comment.details.tags
        if (tags.isEmpty()) return
        buffer.append("<div class='comment-body-content-tags'>")
        buffer.increaseIndent()
        tags.forEach(fun(tag) {
            val tagClassName = tag.value.lowercase(Locale.getDefault()).split(" ").joinToString("-")
            buffer.append("<span class='badge $tagClassName'>${tag.value}</span>")
        })
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    private fun appendCommentBodyContentDescription(
        buffer: HtmlBuffer,
        comment: ReviewComment
    ) {
        val description = comment.details.description
        if (description.isBlank()) return
        buffer.append("<div class='comment-body-content-description'>$description</div>")
    }

    private fun appendCopyright(buffer: HtmlBuffer) {
        buffer.append("<div class='copyright'>")
        buffer.increaseIndent()
        val timestamp = RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(Instant.now())
        buffer.append("Generated with <b>&copy; Senpai - Code Reviews</b> on $timestamp")
        buffer.decreaseIndent()
        buffer.append("</div>")
    }

    override fun closeTag(buffer: HtmlBuffer) {
        buffer.decreaseIndent()
        buffer.append("</div>")
    }
}
