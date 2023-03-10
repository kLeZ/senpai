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

package me.klez.senpai.report.html.node

import me.klez.senpai.model.review.Review
import me.klez.senpai.model.review.comment.ReviewFileComment
import me.klez.senpai.report.html.HtmlBuffer
import org.apache.commons.text.StringEscapeUtils

class SnippetsPanel(private val review: Review) : HtmlNode() {
	override fun openTag(buffer: HtmlBuffer) {
		buffer.append("<div class='snippets-panel'>")
		buffer.increaseIndent()
		appendEmptyState(buffer)
		appendSnippets(buffer)
	}

	private fun appendEmptyState(buffer: HtmlBuffer) {
		buffer.append("<div class='snippets-panel-empty-state-layout'>")
		buffer.increaseIndent()
		buffer.append("<div class='snippets-panel-empty-state'>")
		buffer.increaseIndent()
		appendEmptyStateImage(buffer)
		appendEmptyStateQuote(buffer)
		appendEmptyStateQuoteAuthor(buffer)
		buffer.decreaseIndent()
		buffer.append("</div>")
		buffer.decreaseIndent()
		buffer.append("</div>")
	}

	private fun appendEmptyStateImage(buffer: HtmlBuffer) {
		buffer.append("<img class='snippets-panel-empty-state-image' src='assets/img/senpai.svg' type='image/svg+xml'/>")
	}

	private fun appendEmptyStateQuote(buffer: HtmlBuffer) {
		buffer.append("<div class='snippets-panel-empty-state-quote'>")
		buffer.increaseIndent()
		buffer.append("Notice Me Senpai.")
		buffer.decreaseIndent()
		buffer.append("</div>")
	}

	private fun appendEmptyStateQuoteAuthor(buffer: HtmlBuffer) {
		buffer.append("<div class='snippets-panel-empty-state-quote-author'>")
		buffer.increaseIndent()
		buffer.append("- People on deviantArt -")
		buffer.decreaseIndent()
		buffer.append("</div>")
	}

	private fun appendSnippets(buffer: HtmlBuffer) {
		buffer.append("<div class='snippets'>")
		buffer.increaseIndent()
		review.sortedFilesComments().forEach(fun(_, fileComments) {
			fileComments.forEachIndexed(fun(index, fileComment) {
				appendSnippet(buffer, index, fileComment)
			})
		})
		buffer.decreaseIndent()
		buffer.append("</div>")
	}

	private fun appendSnippet(buffer: HtmlBuffer, index: Int, fileComment: ReviewFileComment) {
		buffer.append("<div data-key='${fileComment.hashCode()}-$index' class='snippet'>")
		buffer.increaseIndent()
		buffer.append("<pre ${getPreProps(fileComment)}><code class='language-${fileComment.brush}'>")
		buffer.increaseIndent()
		buffer.appendWithoutIndent(StringEscapeUtils.escapeHtml4(fileComment.codeSnippet))
		buffer.decreaseIndent()
		buffer.append("</code></pre>")
		buffer.decreaseIndent()
		buffer.append("</div>")
	}

	private fun getPreProps(fileComment: ReviewFileComment) =
		"class='line-numbers' ${getOtherAttributes(fileComment)}"

	private fun getOtherAttributes(fileComment: ReviewFileComment): String {
		val otherAttributes = mutableListOf<String>()
		otherAttributes.add("data-start='${fileComment.startingLine}'")
		otherAttributes.add("data-line='${fileComment.highlightStartingLine}-${fileComment.highlightEndingLine}'")
		otherAttributes.add("style='white-space:pre-wrap;'")
		return otherAttributes.joinToString(" ")
	}

	override fun closeTag(buffer: HtmlBuffer) {
		buffer.decreaseIndent()
		buffer.append("</div>")
	}
}
