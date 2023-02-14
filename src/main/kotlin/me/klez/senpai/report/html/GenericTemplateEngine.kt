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

package me.klez.senpai.report.html

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class GenericTemplateEngine(templateMode: TemplateMode?) {
	private val templateEngine: TemplateEngine = TemplateEngine()

	init {
		val templateResolver = ClassLoaderTemplateResolver(this.javaClass.classLoader)
		templateResolver.templateMode = templateMode
		templateResolver.prefix = "/"
		templateResolver.cacheTTLMs = 3600000L // one hour
		templateResolver.isCacheable = true
		templateEngine.setTemplateResolver(templateResolver)
	}

	fun getTemplate(templateName: String?, parameters: Map<String?, Any?>?): String {
		val ctx = Context()
		parameters?.forEach { (k: String?, v: Any?) -> ctx.setVariable(k, v) }
		ctx.setVariable("now", DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneOffset.UTC)))
		return templateEngine.process(templateName, ctx).trim { it <= ' ' }
	}
}
