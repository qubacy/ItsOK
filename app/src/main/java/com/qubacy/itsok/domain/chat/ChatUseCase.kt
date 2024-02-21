package com.qubacy.itsok.domain.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.data.memento.model.toMemento
import com.qubacy.itsok.domain._common.usecase._common.result.SuccessfulResult
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain._common.usecase._common.result._common.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    errorDataRepository: ErrorDataRepository,
    private val mAnswerDataRepository: AnswerDataRepository,
    private val mMementoDataRepository: MementoDataRepository
) : UseCase(errorDataRepository) {
    open fun getNextMessageWithStage(stage: ChatStage): LiveData<Result<List<Message>>> {
        val resultLiveData = MutableLiveData<Result<List<Message>>>()

        mCoroutineScope.launch(mCoroutineDispatcher) {
            val messages = when (stage) {
                ChatStage.IDLE -> getNextMessagesForIdleStage()
                ChatStage.MEMENTO_OFFERING -> getNextMessagesForMementoOfferingStage()
                ChatStage.BYE -> getNextMessagesForByeStage()
                else -> throw IllegalStateException()
            }

            resultLiveData.postValue(SuccessfulResult(messages) as Result<List<Message>>)
        }

        return resultLiveData
    }

    private fun getNextMessagesForIdleStage(): List<Message> {
        return getNextMessagesForNullTypeStage(ChatStage.IDLE)
    }

    private fun getNextMessagesForMementoOfferingStage(): List<Message> {
        val randomMemento = mMementoDataRepository.getRandomMemento()?.toMemento()
        val answerType = getAnswerTypeByMemento(randomMemento)
        val answer = mAnswerDataRepository
            .getRandomAnswerForStageAndType(ChatStage.MEMENTO_OFFERING, answerType)

        val answerMessage = Message(answer.text)
        val mementoMessage =
            if (randomMemento != null) Message(randomMemento.text, randomMemento.imageUri)
            else null

        return if (mementoMessage == null) listOf(answerMessage)
               else listOf(answerMessage, mementoMessage)
    }

    private fun getAnswerTypeByMemento(memento: Memento?): AnswerType {
        if (memento == null)
            return AnswerType.MEMENTO_OFFERING_NO_MEMENTO

        return if (memento.text == null) {
            if (memento.imageUri == null)
                throw IllegalArgumentException()

            AnswerType.MEMENTO_OFFERING_IMAGE_ONLY

        } else {
            if (memento.imageUri != null)
                AnswerType.MEMENTO_OFFERING_TEXT_AND_IMAGE
            else
                AnswerType.MEMENTO_OFFERING_TEXT_ONLY
        }
    }

    private fun getNextMessagesForByeStage(): List<Message> {
        return getNextMessagesForNullTypeStage(ChatStage.BYE)
    }

    private fun getNextMessagesForNullTypeStage(stage: ChatStage): List<Message> {
        val answer = mAnswerDataRepository
            .getRandomAnswerForStageAndType(stage, null)
        val message = Message(answer.text)

        return listOf(message)
    }
}