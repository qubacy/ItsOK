package com.qubacy.itsok.domain.settings.memento.usecase

import app.cash.turbine.test
import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.data.memento.model.toMemento
import com.qubacy.itsok.data.memento.repository.MementoDataRepository
import com.qubacy.itsok.domain._common.usecase.UseCaseTest
import com.qubacy.itsok.domain.settings.memento.usecase.result.CreateMementoDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.GetMementoesDomainResult
import com.qubacy.itsok.domain.settings.memento.usecase.result.UpdateMementoDomainResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicLong

class PositiveMementoUseCaseTest : UseCaseTest<PositiveMementoUseCase>() {
    protected var mGetAllMementoesResult: List<DataMemento>? = null
    protected var mAddMementoResult: DataMemento? = null
    protected var mUpdateMementoResult: DataMemento? = null
    protected var mRemovedMementoId: AtomicLong = AtomicLong()

    override fun initRepositories(): List<DataRepository> {
        val baseRepositories = super.initRepositories()

        val mementoDataRepositoryMock = Mockito.mock(MementoDataRepository::class.java)

        Mockito.`when`(mementoDataRepositoryMock.getAllMementoes())
            .thenAnswer { mGetAllMementoesResult }
        Mockito.`when`(mementoDataRepositoryMock.addMemento(AnyMockUtil.anyObject()))
            .thenAnswer { mAddMementoResult }
        Mockito.`when`(mementoDataRepositoryMock.updateMemento(AnyMockUtil.anyObject()))
            .thenAnswer { mUpdateMementoResult }
        Mockito.`when`(mementoDataRepositoryMock.deleteMementoById(Mockito.anyLong()))
            .thenAnswer {
                val mementoId = it.arguments[0] as Long

                mRemovedMementoId.set(mementoId)
            }

        return baseRepositories.plus(mementoDataRepositoryMock)
    }

    override fun initUseCase(repositories: List<DataRepository>) {
        mUseCase = PositiveMementoUseCase(
            repositories[0] as ErrorDataRepository,
            repositories[1] as MementoDataRepository
        )
    }

    @Test
    fun getMementoesTest() = runTest {
        val expectedDataMementoes = listOf(
            DataMemento(1, "test")
        )
        val expectedMementoes = expectedDataMementoes.map { it.toMemento() }

        mGetAllMementoesResult = expectedDataMementoes

        mUseCase.resultFlow.test {
            mUseCase.getMementoes()

            val gottenResult = awaitItem()

            Assert.assertEquals(GetMementoesDomainResult::class, gottenResult::class)

            val gottenMementoes = (gottenResult as GetMementoesDomainResult).mementoes

            Assert.assertEquals(expectedMementoes.size, gottenMementoes.size)

            for (expectedMemento in expectedMementoes)
                Assert.assertTrue(gottenMementoes.contains(expectedMemento))
        }
    }

    @Test
    fun createMementoTest() = runTest {
        val expectedDataMemento = DataMemento(1, "test")
        val expectedMemento = expectedDataMemento.toMemento()

        mAddMementoResult = expectedDataMemento

        mUseCase.resultFlow.test {
            mUseCase.createMemento(expectedMemento)

            val gottenResult = awaitItem()

            Assert.assertEquals(CreateMementoDomainResult::class, gottenResult::class)

            val gottenAddedMemento = (gottenResult as CreateMementoDomainResult).memento

            Assert.assertEquals(expectedMemento, gottenAddedMemento)
        }
    }

    @Test
    fun updateMementoTest() = runTest {
        val expectedDataMemento = DataMemento(1, "test")
        val expectedMemento = expectedDataMemento.toMemento()

        mUpdateMementoResult = expectedDataMemento

        mUseCase.resultFlow.test {
            mUseCase.updateMemento(expectedMemento)

            val gottenResult = awaitItem()

            Assert.assertEquals(UpdateMementoDomainResult::class, gottenResult::class)

            val gottenUpdatedMemento = (gottenResult as UpdateMementoDomainResult).memento

            Assert.assertEquals(expectedMemento, gottenUpdatedMemento)
        }
    }

    @Test
    fun removeMementoTest() = runTest {
        val expectedDataMemento = DataMemento(1, "test")

        mUseCase.removeMemento(expectedDataMemento.id!!)

        Assert.assertEquals(expectedDataMemento.id, mRemovedMementoId.get())
    }
}