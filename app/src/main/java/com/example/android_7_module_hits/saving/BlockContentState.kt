package com.example.android_7_module_hits.saving

import com.example.android_7_module_hits.blocks.DataType
import com.example.android_7_module_hits.blocks.FunsType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class BlockContentState {

    @Serializable
    @SerialName("declare")
    data class Declare(
        val type: DataType,
        val name: String,
        val value: String = "0",
        val length: String = "0"
    ) : BlockContentState()

    @Serializable
    @SerialName("assignment")
    data class Assignment(
        val name: String,
        val value: String
    ) : BlockContentState()

    @Serializable
    @SerialName("condition")
    data class Condition(
        val expression: String
    ) : BlockContentState()

    @Serializable
    @SerialName("elseIf")
    data class ElseIf(
        val expression: String
    ) : BlockContentState()

    @Serializable
    @SerialName("while")
    data class While(
        val expression: String
    ) : BlockContentState()

    @Serializable
    @SerialName("for")
    data class For(
        var variable: String,
        var initValue: String,
        var expression: String,
        var construct: String
    ) : BlockContentState()

    @Serializable
    @SerialName("functions")
    data class Functions(
        var func: FunsType,
        var comParam: String,
        var firstSw: String = "a",
        var secondSw: String = "b"
    ) : BlockContentState()

    @Serializable
    @SerialName("end")
    object End : BlockContentState()

    @Serializable
    @SerialName("else")
    object Else : BlockContentState()
}