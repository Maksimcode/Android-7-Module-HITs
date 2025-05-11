package com.example.android_7_module_hits.Blocks

sealed class BlockContent{

    data class Declare(
        var name: String,
        val value: String = "0"
    ) :BlockContent()

    data class Assignment(
        var name: String,
        var value: String
    ) :BlockContent()

    data class Condition(
        var condition: String
    ) :BlockContent()
}
