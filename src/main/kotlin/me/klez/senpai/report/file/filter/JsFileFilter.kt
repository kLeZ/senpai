package me.klez.senpai.report.file.filter

import java.io.File
import java.io.FileFilter

class JsFileFilter : FileFilter {
    override fun accept(file: File?): Boolean {
        file ?: return false
        return file.path.endsWith(".js")
    }
}
