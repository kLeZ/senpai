package me.klez.senpai.report.html

import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver


class GenericTemplateEngine(templateMode: TemplateMode?) {
	private val templateEngine: TemplateEngine = TemplateEngine()

	init {
		val templateResolver = ClassLoaderTemplateResolver(Thread.currentThread().contextClassLoader)
		templateResolver.templateMode = templateMode
		templateResolver.prefix = "/"
		templateResolver.cacheTTLMs = 3600000L // one hour
		templateResolver.isCacheable = true
		templateEngine.setTemplateResolver(templateResolver)
	}

	fun getTemplate(templateName: String?, parameters: Map<String?, Any?>?): String {
		val ctx = Context()
		parameters?.forEach { (k: String?, v: Any?) -> ctx.setVariable(k, v) }
		return templateEngine.process(templateName, ctx).trim { it <= ' ' }
	}
}
