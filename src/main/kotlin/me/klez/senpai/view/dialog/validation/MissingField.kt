package me.klez.senpai.view.dialog.validation

import com.intellij.openapi.ui.Messages

fun missingField(fieldName: String) {
    Messages.showErrorDialog("$fieldName is missing.", "Missing Field")
}
