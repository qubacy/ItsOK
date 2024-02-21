package com.qubacy.itsok.domain._common.usecase._common.result

import com.qubacy.itsok.domain._common.usecase._common.result._common.Result

class SuccessfulResult<T>(
    val data: T
) : Result<T> {

}