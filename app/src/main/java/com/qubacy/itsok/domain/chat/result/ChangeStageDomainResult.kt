package com.qubacy.itsok.domain.chat.result

import com.qubacy.itsok._common.chat.stage.ChatStage
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult

class ChangeStageDomainResult(
    val stage: ChatStage
) : DomainResult {
}