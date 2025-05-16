package com.example.android_7_module_hits

import com.example.android_7_module_hits.Blocks.DataType
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OffsetState(
    val x: Float,
    val y: Float
)

@Serializable
enum class BlockTypeState {
    DECLARE,
    ASSIGN,
    CONDITION,
    END
}

@Serializable
sealed class BlockContentState {
    @Serializable
    @SerialName("declare")
    data class Declare(
        val type: DataType?,
        val name: String,
        val value: String = "0"
    ) : BlockContentState()

    @Serializable
    @SerialName("assignment")
    data class Assignment(
        val name: String,
        val value: String?
    ) : BlockContentState()

    @Serializable
    @SerialName("condition")
    data class Condition(
        val firstPart: String,
        val operator: String,
        val secondPart: String
    ) : BlockContentState()

    @Serializable
    @SerialName("end")
    object End : BlockContentState()
}

@Serializable
data class BlockState(
    val id: String,
    val type: BlockTypeState,
    val content: BlockContentState,
    val position: OffsetState,
    val parentId: String? = null,
    val childId: String? = null,
    val associatedBlockId: String? = null
)
