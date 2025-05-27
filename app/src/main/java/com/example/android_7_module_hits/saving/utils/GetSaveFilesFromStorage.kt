package com.example.android_7_module_hits.saving.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getSaveProjects(context: Context): List<SaveProject> {
    return context.fileList()
        .filter { it.endsWith(".json") }
        .map { fileName ->
            val file = context.getFileStreamPath(fileName)
            val lastModified = file.lastModified()
            val formattedDate = SimpleDateFormat("MMM d,\n yyyy", Locale.getDefault())
                .format(Date(lastModified))
            val displayName = fileName.removeSuffix(".json")
            SaveProject(displayName, formattedDate)
        }
}