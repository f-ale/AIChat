package com.francescoalessi.parla.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.francescoalessi.parla.api.ApiResponse
import com.francescoalessi.parla.repositories.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendMessageWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: Repository
): CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        // Get data from WorkerParameters
        val message = inputData.getString("message") ?: return Result.failure()
        val conversationId = inputData.getInt("conversationId", -1)

        // Error handling for invalid input
        if (conversationId == -1) {
            return Result.failure()
        }

        return try {
            // Fetch character and conversation data
            val conversation = repository.getConversationWithCharacter(conversationId)

            // Send message through the repository
            val response = repository.sendMessage(
                conversation.character,
                conversation.conversation,
                message
            )

            // Handle the response and return appropriate Result
            if(response is ApiResponse.Success)
                Result.success()
            else
                Result.failure()

        } catch (e: Exception) {
            // Handle any exceptions
            Result.failure()

        }
    }

    //TODO: Implement expedited job methods
}