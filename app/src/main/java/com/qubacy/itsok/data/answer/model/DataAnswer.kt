package com.qubacy.itsok.data.answer.model

import com.qubacy.itsok.data.answer.model.type.AnswerType

data class DataAnswer(
    val id: Long,
    val type: AnswerType?,
    val text: String
) {

}