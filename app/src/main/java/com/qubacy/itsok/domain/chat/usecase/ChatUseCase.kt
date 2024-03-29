package com.qubacy.itsok.domain.chat.usecase

import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain.chat.model.Message
import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.AnswerDataRepository
import com.qubacy.itsok.data.memento.model.toMemento
import com.qubacy.itsok.domain.chat.usecase.result.GetNextMessagesDomainResult
import com.qubacy.itsok.domain.settings.memento.model.Memento
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatUseCase @Inject constructor(
    errorDataRepository: ErrorDataRepository,
    private val mAnswerDataRepository: AnswerDataRepository,
    private val mMementoDataRepository: MementoDataRepository
) : UseCase(errorDataRepository) {
    open fun getIntroMessages() {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val messages = getNextMessagesForIdleStage()

            mResultFlow.emit(GetNextMessagesDomainResult(messages))
        }
    }

    open fun getGripeMessages() {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val messages = getNextMessagesForGripeStage()

            mResultFlow.emit(GetNextMessagesDomainResult(messages))
        }
    }

    open fun getMementoMessages() {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val messages = getNextMessagesForMementoOfferingStage()

            mResultFlow.emit(GetNextMessagesDomainResult(messages))
        }
    }

    open fun getByeMessages() {
        mCoroutineScope.launch(mCoroutineDispatcher) {
            val messages = getNextMessagesForByeStage()

            mResultFlow.emit(GetNextMessagesDomainResult(messages))
        }
    }

    private fun getNextMessagesForIdleStage(): List<Message> {
        return getNextMessagesForNullTypeStage(ChatStage.IDLE)
    }

    private fun getNextMessagesForGripeStage(): List<Message> {
        return getNextMessagesForNullTypeStage(ChatStage.GRIPE)
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

        return if (memento.text.isNullOrEmpty()) {
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