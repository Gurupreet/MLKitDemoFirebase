package com.guru.mlkitdemofirebase.data

import ai.api.AIDataService
import ai.api.android.AIConfiguration
import ai.api.android.AIService
import ai.api.model.AIRequest
import ai.api.model.AIResponse
import ai.api.ui.AIDialog
import android.content.Context
import kotlinx.coroutines.*


class ChatBotManager {
   companion object {
       private const val apiAiKey = "6fd4ef55e4a1406c9a7851578702cce0"
       private var INSTANCE: ChatBotManager? = null
       private val config: AIConfiguration = AIConfiguration(apiAiKey,
               ai.api.AIConfiguration.SupportedLanguages.English,
               AIConfiguration.RecognitionEngine.System)
       val aiDataService = AIDataService(config)

       fun get() : ChatBotManager =
               INSTANCE ?: synchronized(this) {
                   INSTANCE
                           ?: ChatBotManager()
               }

   }

    fun getAIService(context: Context): AIService {
        return AIService.getService(context, config)
    }

    fun getAIDialog(context: Context): AIDialog {
        return AIDialog(context, config)
    }

}