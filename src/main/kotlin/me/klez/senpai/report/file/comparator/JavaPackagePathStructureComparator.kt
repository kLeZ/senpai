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

package me.klez.senpai.report.file.comparator

import me.klez.senpai.report.file.FileExtension.JAVA

class JavaPackagePathStructureComparator : PathStructureComparator {
	override fun isApplicable(p0: PathStructure, p1: PathStructure): Boolean {
		return JAVA.matches(p0.extension) && JAVA.matches(p1.extension) && isPackagedFile(p0) && isPackagedFile(p1)
	}

	private fun isPackagedFile(pathStructure: PathStructure): Boolean {
		return pathStructure.containingFolder.contains(Regex("src/main|src/test|src/it"))
	}

	override fun compare(p0: PathStructure, p1: PathStructure): Int {
		return extractPackageName(p0).compareTo(extractPackageName(p1))
	}

	private fun extractPackageName(pathStructure: PathStructure): String {
		val subPartitions = pathStructure.partitions.toMutableList()
		val startingIndex = subPartitions.indexOf("src") + 2
		return subPartitions.slice(IntRange(startingIndex, subPartitions.lastIndex)).joinToString("/")
	}
}
