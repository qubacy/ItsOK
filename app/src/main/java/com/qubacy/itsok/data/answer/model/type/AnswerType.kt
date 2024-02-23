package com.qubacy.itsok.data.answer.model.type

import com.qubacy.itsok._common.chat.stage.ChatStage

enum class AnswerType(
    val stage: ChatStage,
    val id: Int
) {
    MEMENTO_OFFERING_NO_MEMENTO(ChatStage.MEMENTO_OFFERING, 0),
    MEMENTO_OFFERING_TEXT_ONLY(ChatStage.MEMENTO_OFFERING, 1),
    MEMENTO_OFFERING_IMAGE_ONLY(ChatStage.MEMENTO_OFFERING, 2),
    MEMENTO_OFFERING_TEXT_AND_IMAGE(ChatStage.MEMENTO_OFFERING, 3);

    companion object {
        fun getTypeByStageIdAndId(stageId: Int, id: Int): AnswerType? {
            return entries.find { it.stage.id == stageId && it.id == id }
        }
    }
}