package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qubacy.itsok._common._test.util.rule.dispatcher.MainDispatcherRule
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok._common.error.type._test.TestErrorType
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain._common.usecase._common.result._common.DomainResult
import com.qubacy.itsok.domain._common.usecase._common.result.error.ErrorDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.error.ErrorUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

abstract class BaseViewModelTest<
    UiStateType : BaseUiState, UseCaseType: UseCase, ViewModelType : BaseViewModel<UiStateType>
>(
    private val mUseCaseClass: Class<UseCaseType>
) {
    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    protected lateinit var mModel: ViewModelType
    protected lateinit var mResultFlow: MutableSharedFlow<DomainResult>

    protected open fun setUiState(uiState: UiStateType) {
        BaseViewModel::class.java.getDeclaredField("mUiState")
            .apply { isAccessible = true }
            .set(mModel, uiState)
    }

    @Before
    fun setup() {
        init()
    }

    private fun init() {
        resetResults()

        val useCase = initUseCase()

        initViewModel(useCase)
    }

    protected open fun resetResults() {

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

    protected open fun initViewModel(
        useCase: UseCaseType
    ) {
        val savedStateHandleMock = Mockito.mock(SavedStateHandle::class.java)
        val errorDataRepositoryMock = Mockito.mock(ErrorDataRepository::class.java)

        Mockito.`when`(errorDataRepositoryMock.getError(Mockito.anyLong()))
            .thenAnswer {
                val errorTypeId = it.arguments[0] as Long

                return@thenAnswer Error(errorTypeId, "", false)
            }

        mModel = createViewModel(savedStateHandleMock, errorDataRepositoryMock, useCase)
    }

    protected abstract fun createViewModel(
        savedStateHandle: SavedStateHandle,
        errorDataRepository: ErrorDataRepository,
        useCase: UseCaseType
    ): ViewModelType

    @Test
    fun retrieveErrorTest() = runTest {
        val expectedErrorType = TestErrorType.TEST

        mModel.uiOperationFlow.test {
            mModel.retrieveError(expectedErrorType)

            val errorOperation = awaitItem()

            Assert.assertEquals(ErrorUiOperation::class, errorOperation::class)

            val error = (errorOperation as ErrorUiOperation).error

            Assert.assertEquals(expectedErrorType.id, error.id)
        }
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