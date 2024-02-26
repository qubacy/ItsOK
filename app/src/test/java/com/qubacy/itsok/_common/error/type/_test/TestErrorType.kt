package com.qubacy.itsok._common.error.type._test

import com.qubacy.itsok._common.error.type._common.ErrorType

enum class TestErrorType(override val id: Long) : ErrorType {
    TEST(0);
}