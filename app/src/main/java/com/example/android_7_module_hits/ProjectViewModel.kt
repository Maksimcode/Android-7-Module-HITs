package com.example.android_7_module_hits

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ProjectViewModel : ViewModel() {
    val myProjects = mutableStateListOf<ProjectPreview>()

    fun addProject(project: ProjectPreview) {
        myProjects.add(project)
    }
}