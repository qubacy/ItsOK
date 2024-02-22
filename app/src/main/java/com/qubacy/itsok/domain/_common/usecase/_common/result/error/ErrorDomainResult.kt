package com.qubacy.itsok.domain._common.usecase._common.result.error

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult

class ErrorDomainResult(
    val error: Error
) : DomainResult {

}