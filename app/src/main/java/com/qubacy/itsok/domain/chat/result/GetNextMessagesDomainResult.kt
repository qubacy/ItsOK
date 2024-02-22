package com.qubacy.itsok.domain.chat.result

import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.chat.model.Message

class GetNextMessagesDomainResult(
    val messages: List<Message>
) : DomainResult {

}