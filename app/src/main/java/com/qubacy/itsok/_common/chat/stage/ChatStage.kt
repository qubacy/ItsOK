package com.qubacy.itsok._common.chat.stage

enum class ChatStage(val id: Int) {
    IDLE(0), GRIPE(1), THINKING(2), MEMENTO_OFFERING(3), BYE(4);

    companion object {
        fun getStageById(id: Int): ChatStage {
            return (ChatStage.entries.find { it.id == id }!!)
        }
    }
}