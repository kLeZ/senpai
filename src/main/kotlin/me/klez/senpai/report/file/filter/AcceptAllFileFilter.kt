package me.klez.senpai.report.file.filter

import java.io.File
import java.io.FileFilter

class AcceptAllFileFilter : FileFilter {
    override fun accept(file: File?): Boolean {
       return true
    }
}
