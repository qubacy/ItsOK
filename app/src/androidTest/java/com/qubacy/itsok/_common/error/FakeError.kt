package com.qubacy.itsok._common.error

import com.qubacy.itsok._common.error.type.FakeErrorType

object FakeError {
    val normal = Error(
        FakeErrorType.FAKE.id, "normal fake error", false
    )
    val critical = Error(
        FakeErrorType.FAKE.id, "critical fake error", true
    )
}