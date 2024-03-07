package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model

import app.cash.turbine.test
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok._common.error.type._test.TestErrorType
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain._common.usecase._common.result.error.ErrorDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.StatefulViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.StatefulViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.state.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

abstract class BusinessViewModelTest<
    UiStateType : BaseUiState,
    UseCaseType: UseCase,
    ViewModelType : StatefulViewModel<UiStateType>
>(
    private val mUseCaseClass: Class<UseCaseType>
) : StatefulViewModelTest<UiStateType, ViewModelType>() {
    protected lateinit var mUseCase: UseCaseType
    protected lateinit var mResultFlow: MutableSharedFlow<DomainResult>

    override fun preInit() {
        mUseCase = initUseCase()
    }

    protected open fun initUseCase(): UseCaseType {
        val useCase = createUseCaseMock()

        mResultFlow = MutableSharedFlow()

        Mockito.`when`(useCase.resultFlow).thenReturn(mResultFlow)

        return useCase
    }

    private fun createUseCaseMock(): UseCaseType {
        return Mockito.mock(mUseCaseClass)
    }

    @Test
    fun processErrorDomainResultTest() = runTest {
        val expectedError = Error(TestErrorType.TEST.id, "test error", false)
        val errorResult = ErrorDomainResult(expectedError)
        val expectedLoadingState = false

        mModel.uiOperationFlow.test {
            mResultFlow.emit(errorResult)

            val gottenErrorOperation = awaitItem()
            val gottenEndLoadingOperation = awaitItem()

            Assert.assertEquals(ErrorUiOperation::class, gottenErrorOperation::class)
            Assert.assertEquals(SetLoadingStateUiOperation::class, gottenEndLoadingOperation::class)

            val gottenError = (gottenErrorOperation as ErrorUiOperation).error
            val gottenLoadingState = (gottenEndLoadingOperation as SetLoadingStateUiOperation).isLoading

            Assert.assertEquals(expectedError, gottenError)
            Assert.assertEquals(expectedLoadingState, gottenLoadingState)
        }
    }
}