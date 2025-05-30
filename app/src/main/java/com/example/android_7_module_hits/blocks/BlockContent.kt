package com.example.android_7_module_hits.blocks

sealed class BlockContent {

    data class Declare(
        var type: DataType,
        var name: String,
        val value: String = "0",
        var length: String = "0"
    ) : BlockContent()

    data class Assignment(
        var name: String,
        var value: String
    ) : BlockContent()

    data class Condition(
        var expression: String
    ) : BlockContent()

    data class ElseIf(
        var expression: String
    ) : BlockContent()

    data class While(
        var expression: String
    ) : BlockContent()

    data class For(
        var variable: String,
        var initValue: String,
        var expression: String,
        var construct: String
    ) : BlockContent()

    data class Functions(
        var func: FunsType,
        var comParam: String,
        var firstSw: String = "a",
        var secondSw: String = "b"
    ) : BlockContent()

    class End() : BlockContent()

    class Else() : BlockContent()
}
