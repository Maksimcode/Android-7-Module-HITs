package com.example.android_7_module_hits.Blocks

sealed class BlockContent{

    data class Declare(
        var type: DataType,
        var name: String,
        val value: String = "0"
    ) :BlockContent()

    data class Assignment(
        var name: String,
        var value: String
    ) :BlockContent()

    data class Condition(
        var expression: String
    ) :BlockContent()

    data class ElseIf (
        var expression: String
    ) : BlockContent()

    class End() : BlockContent()

    class Else() : BlockContent()
}
