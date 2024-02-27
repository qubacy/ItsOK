package com.qubacy.itsok.data._common.repository.adjustable

import com.qubacy.itsok._common.coroutine.CoroutineUser
import com.qubacy.itsok.data._common.repository._common.DataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

abstract class AdjustableDataRepository(
    coroutineScope: CoroutineScope,
    coroutineDispatcher: CoroutineDispatcher
) : CoroutineUser(coroutineScope, coroutineDispatcher), DataRepository {

}