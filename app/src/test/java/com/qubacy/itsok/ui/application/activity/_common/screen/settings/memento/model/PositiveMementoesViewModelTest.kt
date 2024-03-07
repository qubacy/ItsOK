package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.qubacy.itsok._common._test._common.util.mock.AnyMockUtil
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain.settings.memento.model._test.usecase.MementoGeneratorUtil
import com.qubacy.itsok.domain.settings.memento.usecase.PositiveMementoUseCase
import com.qubacy.itsok.domain.settings.memento.usecase.result.CreateMementoDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.UpdateMementoDomainResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation.loading.SetLoadingStateUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.business.model.BusinessViewModelTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.AddMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.SetMementoesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.UpdateMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicBoolean

class PositiveMementoesViewModelTest(

) : BusinessViewModelTest<
    PositiveMementoesUiState,
    PositiveMementoUseCase,
    PositiveMementoesViewModel
>(PositiveMementoUseCase::class.java) {
    private val mGetMementoesCallFlag: AtomicBoolean = AtomicBoolean(false)
    private val mCreateMementoCallFlag: AtomicBoolean = AtomicBoolean(false)
    private val mUpdateMementoesCallFlag: AtomicBoolean = AtomicBoolean(false)
    private val mRemoveMementoesCallFlag: AtomicBoolean = AtomicBoolean(false)

    override fun initUseCase(): PositiveMementoUseCase {
        val useCaseMock = super.initUseCase()

        Mockito.`when`(useCaseMock.getMementoes())
            .thenAnswer { mGetMementoesCallFlag.set(true) }
        Mockito.`when`(useCaseMock.createMemento(AnyMockUtil.anyObject()))
            .thenAnswer { mCreateMementoCallFlag.set(true) }
        Mockito.`when`(useCaseMock.updateMemento(AnyMockUtil.anyObject()))
            .thenAnswer { mUpdateMementoesCallFlag.set(true) }
        Mockito.`when`(useCaseMock.removeMemento(Mockito.anyLong()))
            .thenAnswer { mRemoveMementoesCallFlag.set(true) }

        return useCaseMock
    }

    override fun resetResults() {
        super.resetResults()

        mGetMementoesCallFlag.set(false)
        mCreateMementoCallFlag.set(false)
        mUpdateMementoesCallFlag.set(false)
        mRemoveMementoesCallFlag.set(false)
    }

    override fun createViewModel(
        savedStateHandle: SavedStateHandle,
        errorDataRepository: ErrorDataRepository
    ): PositiveMementoesViewModel {
        return PositiveMementoesViewModel(savedStateHandle, errorDataRepository, mUseCase)
    }

    @Test
    fun getMementoesTest() = runTest {
        val initUiState = PositiveMementoesUiState()

        setUiState(initUiState)

        val expectedLoadingState = true

        mModel.uiOperationFlow.test {
            mModel.getMementoes()

            val gottenOperation = awaitItem()

            Assert.assertEquals(SetLoadingStateUiOperation::class, gottenOperation::class)

            val gottenLoadingState = (gottenOperation as SetLoadingStateUiOperation).isLoading

            Assert.assertEquals(expectedLoadingState, gottenLoadingState)
            Assert.assertEquals(expectedLoadingState, mModel.uiState.isLoading)

            Assert.assertTrue(mGetMementoesCallFlag.get())
        }
    }

    @Test
    fun getMementoByIdTest() {
        val expectedMementoes = MementoGeneratorUtil.generateMementoes(3, true)
        val initUiState = PositiveMementoesUiState(mementoes = expectedMementoes)

        setUiState(initUiState)

        for (expectedMemento in expectedMementoes)
            mModel.getMementoById(expectedMemento.id!!)
    }

    @Test
    fun createMementoTest() {
        val expectedMemento = MementoGeneratorUtil.generateMemento()

        mModel.createMemento(expectedMemento)

        Assert.assertTrue(mCreateMementoCallFlag.get())
    }

    @Test
    fun updateMementoTest() {
        val expectedMemento = MementoGeneratorUtil.generateMemento()

        mModel.updateMemento(expectedMemento)

        Assert.assertTrue(mUpdateMementoesCallFlag.get())
    }

    @Test
    fun removeMementoTest() {
        val expectedMemento = MementoGeneratorUtil.generateMemento(id = 1)

        mModel.removeMemento(expectedMemento.id!!)

        Assert.assertTrue(mRemoveMementoesCallFlag.get())
    }

    @Test
    fun processGetMementoesDomainResultTest() = runTest {
        val initUiState = PositiveMementoesUiState(
            isLoading = true
        )

        setUiState(initUiState)

        val expectedLoadingState = false
        val expectedMementoes = MementoGeneratorUtil.generateMementoes(3, true)
        val expectedGetMementoesDomainResult = GetMementoesDomainResult(expectedMementoes)

        mModel.uiOperationFlow.test {
            mResultFlow.emit(expectedGetMementoesDomainResult)

            val gottenMementoesOperation = awaitItem()
            val gottenLoadingStateOperation = awaitItem()

            Assert.assertEquals(SetLoadingStateUiOperation::class, gottenLoadingStateOperation::class)
            Assert.assertEquals(SetMementoesUiOperation::class, gottenMementoesOperation::class)

            val gottenLoadingState = (gottenLoadingStateOperation as SetLoadingStateUiOperation)
                .isLoading
            val gottenMementoes = (gottenMementoesOperation as SetMementoesUiOperation).mementoes
            val gottenUiState = mModel.uiState

            Assert.assertEquals(expectedLoadingState, gottenLoadingState)
            Assert.assertEquals(expectedLoadingState, gottenUiState.isLoading)
            Assert.assertEquals(expectedMementoes.size, gottenMementoes.size)
            Assert.assertEquals(expectedMementoes.size, gottenUiState.mementoes.size)

            for (expectedMemento in expectedMementoes) {
                Assert.assertTrue(gottenMementoes.contains(expectedMemento))
                Assert.assertTrue(gottenUiState.mementoes.contains(expectedMemento))
            }
        }
    }

    @Test
    fun processCreateMementoDomainResultTest() = runTest {
        val initMementoes = MementoGeneratorUtil.generateMementoes(2, true)
        val initUiState = PositiveMementoesUiState(
            mementoes = initMementoes
        )

        setUiState(initUiState)

        val expectedMemento = MementoGeneratorUtil.generateMemento(initMementoes.size.toLong())
        val expectedMementoes = initMementoes.plus(expectedMemento)
        val expectedCreateMementoDomainResult = CreateMementoDomainResult(expectedMemento)

        mModel.uiOperationFlow.test {
            mResultFlow.emit(expectedCreateMementoDomainResult)

            val gottenMementoOperation = awaitItem()

            Assert.assertEquals(AddMementoUiOperation::class, gottenMementoOperation::class)

            val gottenMemento = (gottenMementoOperation as AddMementoUiOperation).memento
            val gottenUiState = mModel.uiState

            Assert.assertEquals(expectedMemento, gottenMemento)
            Assert.assertEquals(expectedMementoes.size, gottenUiState.mementoes.size)

            for (currentExpectedMemento in expectedMementoes) {
                Assert.assertTrue(gottenUiState.mementoes.contains(currentExpectedMemento))
            }
        }
    }

    @Test
    fun processUpdateMementoDomainResultTest() = runTest {
        val initMementoes = MementoGeneratorUtil.generateMementoes(2, true)
        val initUiState = PositiveMementoesUiState(
            mementoes = initMementoes
        )

        setUiState(initUiState)

        val expectedMemento = initMementoes[0].copy(text = "updated memento")
        val expectedMementoes = initMementoes.toMutableList().apply { this[0] = expectedMemento }
        val expectedUpdateMementoDomainResult = UpdateMementoDomainResult(expectedMemento)

        mModel.uiOperationFlow.test {
            mResultFlow.emit(expectedUpdateMementoDomainResult)

            val gottenMementoOperation = awaitItem()

            Assert.assertEquals(UpdateMementoUiOperation::class, gottenMementoOperation::class)

            val gottenMemento = (gottenMementoOperation as UpdateMementoUiOperation).memento
            val gottenUiState = mModel.uiState

            Assert.assertEquals(expectedMemento, gottenMemento)
            Assert.assertEquals(expectedMementoes.size, gottenUiState.mementoes.size)

            for (currentExpectedMemento in expectedMementoes) {
                Assert.assertTrue(gottenUiState.mementoes.contains(currentExpectedMemento))
            }
        }
    }
}