package com.qubacy.itsok.domain._common.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok._common.error.type._common.ErrorType
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class UseCase(
    protected val mErrorDataRepository: ErrorDataRepository,
    protected var mCoroutineScope: CoroutineScope = GlobalScope,
    protected var mCoroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun setCoroutineScope(coroutineScope: CoroutineScope) {
        mCoroutineScope = coroutineScope
    }

    fun setCoroutineDispatcher(coroutineDispatcher: CoroutineDispatcher) {
        mCoroutineDispatcher = coroutineDispatcher
    }

    open fun retrieveError(errorType: ErrorType): LiveData<Error> {
        val resultLiveData = MutableLiveData<Error>()

        mCoroutineScope.launch(mCoroutineDispatcher) {
            val error = mErrorDataRepository.getError(errorType.id)

            resultLiveData.postValue(error)
        }

        return resultLiveData
    }
}