package com.qubacy.itsok.domain._common.usecase._common.result

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.domain._common.usecase._common.result._common.Result

class ErrorResult<T>(
    val error: Error
) : Result<T> {

}