package com.qubacy.itsok.domain.settings.memento.usecase.result

import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain.settings.memento.model.Memento

class UpdateMementoDomainResult(
    val memento: Memento
) : DomainResult {

}