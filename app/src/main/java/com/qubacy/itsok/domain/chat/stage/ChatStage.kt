package com.qubacy.itsok.domain.chat.stage

enum class ChatStage(val id: Int) {
    IDLE(0), THINKING(1), MEMENTO_OFFERING(2), BYE(3);

    companion object {
        fun getStageById(id: Int): ChatStage {
            return (ChatStage.entries.find { it.id == id }!!)
        }
    }
}