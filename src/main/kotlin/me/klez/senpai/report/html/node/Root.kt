package me.klez.senpai.report.html.node

import me.klez.senpai.report.html.HtmlBuffer

class Root : HtmlNode() {
	override fun openTag(buffer: HtmlBuffer) {
		buffer.append("<html>")
		buffer.increaseIndent()
	}

	override fun closeTag(buffer: HtmlBuffer) {
		buffer.decreaseIndent()
		buffer.append("</html>")
	}
}
