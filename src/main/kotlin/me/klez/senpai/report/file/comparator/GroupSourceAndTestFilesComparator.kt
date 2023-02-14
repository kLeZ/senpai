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

package me.klez.senpai.report.file.comparator

import me.klez.senpai.report.file.extractPathStructure

interface PathStructureComparator : Comparator<PathStructure> {
	fun isApplicable(p0: PathStructure, p1: PathStructure): Boolean
}

private val pathStructureComparators = listOf(JavaPackagePathStructureComparator(), DefaultPathStructureComparator())

class GroupSourceAndTestFilesPathComparator : Comparator<String> {
	override fun compare(filePath0: String, filePath1: String): Int {
		val pathStructure0 = extractPathStructure(filePath0)
		val pathStructure1 = extractPathStructure(filePath1)
		return pathStructureComparators.find(fun(comparator): Boolean {
			return comparator.isApplicable(pathStructure0, pathStructure1)
		})!!.compare(pathStructure0, pathStructure1)
	}
}
