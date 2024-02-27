package com.qubacy.itsok.data.answer.repository

import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.answer.model.DataAnswer
import com.qubacy.itsok.data.answer.model.type.AnswerType
import com.qubacy.itsok.data.answer.repository.source.local.LocalAnswerDataSource
import com.qubacy.itsok.data.answer.repository.source.local.entity.toDataAnswer
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class AnswerDataRepository @Inject constructor(
    private val mLocalAnswerDataSource: LocalAnswerDataSource
) : DataRepository {
    fun getRandomAnswerForStageAndType(
        chatStage: ChatStage, type: AnswerType?
    ): DataAnswer {
        val lang = Locale.getDefault().language
        val answers =
            if (type == null) mLocalAnswerDataSource.getAnswersForLangAndStage(lang, chatStage.id)
            else mLocalAnswerDataSource.getAnswersForLangStageAndType(lang, chatStage.id, type.id)

        if (answers.isEmpty()) throw IllegalStateException()
        if (answers.size < 2) return answers.first().toDataAnswer()

        val randomAnswerIndex = Random(System.currentTimeMillis())
            .nextInt(0, answers.size - 1)

        return answers[randomAnswerIndex].toDataAnswer()
    }
}