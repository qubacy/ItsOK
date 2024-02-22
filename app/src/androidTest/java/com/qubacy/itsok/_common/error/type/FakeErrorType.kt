package com.qubacy.itsok._common.error.type

import com.qubacy.itsok._common.error.type._common.ErrorType

enum class FakeErrorType(
    override val id: Long
) : ErrorType {
    FAKE(0);
}