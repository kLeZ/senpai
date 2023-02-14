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

package me.klez.senpai.report.file

import me.klez.senpai.report.file.comparator.PathStructure
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun createDirectories(_path: String) {
	val path = Paths.get(_path)
	if (!Files.exists(path)) {
		Files.createDirectories(path)
	}
}

fun deleteDirectory(_path: String) {
	val directoryToBeDeleted = File(_path)
	val allContents: Array<File>? = directoryToBeDeleted.listFiles()
	if (allContents != null) {
		for (file in allContents) {
			deleteDirectory(file.path)
		}
	}
	directoryToBeDeleted.delete()
}

fun replaceInFile(_path: String, oldText: String, newText: String) {
	val path = Paths.get(_path)
	if (!Files.exists(path)) return
	val content = String(Files.readAllBytes(path))
	val editedContent = content.replace(oldText, newText)
	Files.write(path, editedContent.toByteArray())
}

fun extractPathStructure(filePath: String): PathStructure {
	val partitions = filePath.split("/").filter(fun(partition) = partition != "")
	val containingFolder = partitions.subList(0, partitions.lastIndex).joinToString("/")
	val fileName = partitions.last()
	val fileNamePartitions = fileName.split(".")
	val extension = fileNamePartitions.slice(IntRange(1, fileNamePartitions.lastIndex)).joinToString(".")
	return PathStructure(filePath, partitions, containingFolder, fileName, extension)
}
