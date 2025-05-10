package com.example.android_7_module_hits.Blocks

sealed class BlockContent

data class Declare(
    val name: String
) : BlockContent()

data class Assignment(
    val name: String,
    val value: String
) : BlockContent()