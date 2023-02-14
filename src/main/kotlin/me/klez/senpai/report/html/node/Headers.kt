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

import me.klez.senpai.report.file.filter.CssFileFilter
import me.klez.senpai.report.file.filter.JsFileFilter
import me.klez.senpai.report.file.getAllResourcesPathRecursively
import me.klez.senpai.report.html.HtmlBuffer

class Headers : HtmlNode() {
	override fun openTag(buffer: HtmlBuffer) {
		buffer.append("<head>")
		buffer.increaseIndent()
		appendTitle(buffer)
		appendCssFiles(buffer)
		appendJsFiles(buffer)
		appendFaviconImages(buffer)
		appendMeta(buffer)
	}

	private fun appendTitle(buffer: HtmlBuffer) {
		buffer.append("<title>")
		buffer.increaseIndent()
		buffer.append("Senpai - Code Reviews")
		buffer.decreaseIndent()
		buffer.append("</title>")
	}

	private fun appendCssFiles(buffer: HtmlBuffer) {
		val cssResources =
			getAllResourcesPathRecursively(
				this.javaClass.classLoader, "assets",
				CssFileFilter()
			)
		cssResources.forEach(fun(cssResource) {
			buffer.append("<link rel='stylesheet' href='$cssResource'/>")
		})
	}

	private fun appendJsFiles(buffer: HtmlBuffer) {
		val jsResources =
			getAllResourcesPathRecursively(this.javaClass.classLoader, "assets", JsFileFilter())
		jsResources.forEach(fun(jsResource) {
			buffer.append("<script src='$jsResource'></script>")
		})
	}

	private fun appendFaviconImages(buffer: HtmlBuffer) {
		buffer.append("<link rel='apple-touch-icon' sizes='180x180' href='assets/img/favicon/apple-touch-icon.png'>")
		buffer.append("<link rel='icon' type='image/png' sizes='32x32' href='assets/img/favicon/favicon-32x32.png'>")
		buffer.append("<link rel='icon' type='image/png' sizes='16x16' href='assets/img/favicon/favicon-16x16.png'>")
		buffer.append("<link rel='manifest' href='assets/img/favicon/site.webmanifest'>")
		buffer.append("<link rel='mask-icon' href='assets/img/favicon/safari-pinned-tab.svg' color='#65828a'>")
		buffer.append("<link rel='shortcut icon' href='assets/img/favicon/favicon.ico'>")
		buffer.append("<meta name='apple-mobile-web-app-title' content='Senpai'>")
		buffer.append("<meta name='application-name' content='Senpai'>")
		buffer.append("<meta name='msapplication-TileColor' content='#da532c'>")
		buffer.append("<meta name='msapplication-config' content='assets/img/favicon/browserconfig.xml'>")
		buffer.append("<meta name='theme-color' content='#ffffff'>")
	}

	private fun appendMeta(buffer: HtmlBuffer) {
		buffer.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>")
	}

	override fun closeTag(buffer: HtmlBuffer) {
		buffer.decreaseIndent()
		buffer.append("</head>")
	}
}
