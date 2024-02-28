package com.qubacy.itsok.domain._common.usecase._common

import com.qubacy.itsok._common.coroutine.CoroutineUser
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class UseCase(
    protected val mErrorDataRepository: ErrorDataRepository,
    mCoroutineScope: CoroutineScope = GlobalScope,
    mCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineUser(mCoroutineScope, mCoroutineDispatcher) {
    protected val mResultFlow: MutableSharedFlow<DomainResult> = MutableSharedFlow()
    val resultFlow: SharedFlow<DomainResult> get() = mResultFlow
}