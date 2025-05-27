package com.example.android_7_module_hits.saving

import android.content.Context
import com.example.android_7_module_hits.blocks.Block
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer

class BlockRepository(private val context: Context) {

    private val json = Json {
        classDiscriminator = "kind"
        prettyPrint = true
        ignoreUnknownKeys = true }

    suspend fun deleteSaveFile(fileName: String) {
        withContext(Dispatchers.IO) {
            context.deleteFile(fileName)
        }
    }

    suspend fun saveBlocks(blocks: List<Block>, fileName: String) {
        withContext(Dispatchers.IO) {
            val blockStates = blocks.map { it.toBlockState() }
            val jsonData = json.encodeToString(ListSerializer(BlockState.serializer()), blockStates)
            context.openFileOutput(fileName, Context.MODE_PRIVATE).use { output ->
                output.write(jsonData.toByteArray())
            }
        }
    }

    suspend fun loadBlocks(fileName: String): List<Block>? {
        return withContext(Dispatchers.IO) {
            try {
                context.openFileInput(fileName).bufferedReader().use { reader ->
                    val jsonData = reader.readText()
                    val blockStates = json.decodeFromString(ListSerializer(BlockState.serializer()), jsonData)
                    blockStates.map { it.toBlock() }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
