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

package me.klez.senpai.report.file

enum class FileExtension(val values: List<String>, val brush: String) {
	BASH(listOf("sh"), "bash"),
	C(listOf("c"), "c"),
	CLOJURE(listOf("clojure", "clj"), "clojure"),
	CPP(listOf("cpp"), "cpp"),
	CSS(listOf("css"), "css"),
	DOTNET(listOf("cs"), "dotnet"),
	GRAPHQL(listOf("graphql"), "graphql"),
	HTML(listOf("html"), "html"),
	JAVA(listOf("java"), "java"),
	JAVASCRIPT(listOf("js"), "javascript"),
	JSON(listOf("json"), "json"),
	KOTLIN(listOf("kt"), "kotlin"),
	LATEX(listOf("tex"), "latex"),
	LUA(listOf("lua"), "lua"),
	MARKDOWN(listOf("md"), "markdown"),
	PERL(listOf("pl"), "perl"),
	PLAIN(listOf("txt"), "none"),
	PYTHON(listOf("py"), "python"),
	RUBY(listOf("rb"), "ruby"),
	RUST(listOf("rs"), "rust"),
	SASS(listOf("sass"), "sass"),
	SCSS(listOf("scss"), "scss"),
	SQL(listOf("sql"), "sql"),
	SWIFT(listOf("swift"), "swift"),
	TYPESCRIPT(listOf("ts"), "typescript"),
	XML(listOf("xml"), "xml"),
	YAML(listOf("yaml", "yml"), "yaml");

	fun matches(extension: String): Boolean {
		return values.contains(extension)
	}

	companion object {
		fun fromExtension(extension: String): FileExtension {
			return values().find(fun(fileExtension: FileExtension): Boolean {
				return fileExtension.values.contains(extension)
			}) ?: PLAIN
		}
	}
}
