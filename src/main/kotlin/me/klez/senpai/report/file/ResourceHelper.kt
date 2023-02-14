package me.klez.senpai.report.file

import me.klez.senpai.report.file.filter.AcceptAllFileFilter
import java.io.File
import java.io.FileFilter
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import java.util.jar.JarFile
import kotlin.streams.toList

fun getAllResourcesPathRecursively(
	classLoader: ClassLoader,
	resourcePath: String,
	filter: FileFilter = AcceptAllFileFilter()
): List<String> {
	val entries = getResourcesEntries(classLoader, resourcePath)
	val resourcesPath = mutableListOf<String>()
	while (entries.hasMoreElements()) {
		val entry = entries.nextElement()
		val entryPath = entry.path
		if (filter.accept(File(entryPath))) resourcesPath.add(entryPath)
	}
	return resourcesPath.toList()
}

fun copyResourcesRecursively(classLoader: ClassLoader, resourcePath: String, outputPath: String) {
	val entries = getResourcesEntries(classLoader, resourcePath)
	while (entries.hasMoreElements()) {
		val entry = entries.nextElement()
		val entryPath = entry.path
		if (entryPath.startsWith(resourcePath) && !entry.isDirectory) {
			copyResourceFile(classLoader, entryPath, outputPath)
		}
	}
}

private fun getResourcesEntries(classLoader: ClassLoader, resourcePath: String): Enumeration<FileEntry> {
	val assetsPath = classLoader.getResources(resourcePath).nextElement().path
	val list = if (assetsPath.endsWith(".jar!/$resourcePath")) jarEntries(assetsPath, resourcePath) else fileEntries(assetsPath, resourcePath)
	return Collections.enumeration(list)
}

private fun jarEntries(assetsPath: String, resourcePath: String): List<FileEntry> {
	val jarPath = assetsPath.replace("file:", "").replace("!/$resourcePath", "")
	val jarFile = JarFile(jarPath)
	val list = jarFile.entries().toList().map { file ->
		FileEntry(file.name, file.name, file.isDirectory)
	}.toList()
	return list
}

private fun fileEntries(assetsPath: String, resourcePath: String): List<FileEntry> {
	val basepath = assetsPath.replace("file:", "").replace(resourcePath, "")
	val base = File(basepath)
	val list = Files.walk(Paths.get(basepath)).use { walk ->
		walk.map { path ->
			val file = path.toFile()
			FileEntry(file.toRelativeString(base), file.name, file.isDirectory)
		}.toList()
	}
	return list
}

fun copyResourceFile(classLoader: ClassLoader, resourcePath: String, outputPath: String) {
	val inputStream = classLoader.getResourceAsStream(resourcePath) ?: return
	val inputStreamReader = inputStream.readBytes()
	val pathPartition = resourcePath.split("/")
	val parentDirectory = pathPartition.subList(0, pathPartition.lastIndex).joinToString("/")
	createDirectories("$outputPath/$parentDirectory")
	val outputStream = FileOutputStream(File("$outputPath/$resourcePath"))
	outputStream.write(inputStreamReader)
	outputStream.flush()
	outputStream.close()
}

data class FileEntry(val path: String, val name: String, val isDirectory: Boolean)
