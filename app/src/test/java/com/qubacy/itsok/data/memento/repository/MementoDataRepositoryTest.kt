package com.qubacy.itsok.data.memento.repository

import com.qubacy.itsok._common._test.util.mock.AnyMockUtil
import com.qubacy.itsok.data._common.repository.DataRepositoryTest
import com.qubacy.itsok.data.memento.model.DataMemento
import com.qubacy.itsok.data.memento.model.toMementoEntity
import com.qubacy.itsok.data.memento.repository.source.local.LocalMementoDataSource
import com.qubacy.itsok.data.memento.repository.source.local.entity.MementoEntity
import com.qubacy.itsok.data.memento.repository.source.local.entity.toDataMemento
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.atomic.AtomicReference

class MementoDataRepositoryTest : DataRepositoryTest<MementoDataRepository>() {
    @Before
    fun setup() {
        initRepository()
    }

    private fun initRepository(
        getMementoByIdResult: MementoEntity? = null,
        getMementoes: List<MementoEntity> = listOf(),
        insertMementoResult: AtomicReference<MementoEntity?> = AtomicReference<MementoEntity?>(null),
        updateMementoResult: AtomicReference<MementoEntity?> = AtomicReference<MementoEntity?>(null),
        deleteMementoByIdResult: AtomicReference<Long?> = AtomicReference<Long?>(null),
    ) {
        val localMementoDataSourceMock = Mockito.mock(LocalMementoDataSource::class.java)

        Mockito.`when`(localMementoDataSourceMock.getMementoById(Mockito.anyLong()))
            .thenReturn(getMementoByIdResult)
        Mockito.`when`(localMementoDataSourceMock.getMementoes())
            .thenReturn(getMementoes)
        Mockito.`when`(localMementoDataSourceMock.insertMemento(AnyMockUtil.anyObject()))
            .thenAnswer {
                val memento = it.arguments[0] as MementoEntity

                insertMementoResult.set(memento)

                return@thenAnswer memento.id
            }
        Mockito.`when`(localMementoDataSourceMock.updateMemento(AnyMockUtil.anyObject()))
            .thenAnswer {
                val memento = it.arguments[0] as MementoEntity

                updateMementoResult.set(memento)
            }
        Mockito.`when`(localMementoDataSourceMock.deleteMementoById(Mockito.anyLong()))
            .thenAnswer {
                val mementoId = it.arguments[0] as Long

                deleteMementoByIdResult.set(mementoId)
            }

        mDataRepository = MementoDataRepository(localMementoDataSourceMock)
    }

    @Test
    fun getMementoByIdTest() {
        val expectedMementoEntity = MementoEntity(
            1, "test memento")
        val expectedMemento = expectedMementoEntity.toDataMemento()

        initRepository(getMementoByIdResult = expectedMementoEntity)

        val gottenMemento = mDataRepository.getMementoById(expectedMementoEntity.id)

        Assert.assertEquals(expectedMemento, gottenMemento)
    }

    @Test
    fun getMementoesTest() {
        val expectedMementoEntity = MementoEntity(1, "test memento 1")
        val expectedMemento = expectedMementoEntity.toDataMemento()

        initRepository(getMementoes = listOf(expectedMementoEntity))

        val gottenMemento = mDataRepository.getRandomMemento()

        Assert.assertEquals(expectedMemento, gottenMemento)
    }

    @Test
    fun insertMementoTest() {
        val mementoToInsert = DataMemento(1, "test memento")
        val insertMementoResult = AtomicReference<MementoEntity?>(null)
        val getMementoByIdResult = mementoToInsert.toMementoEntity()

        initRepository(
            insertMementoResult = insertMementoResult,
            getMementoByIdResult = getMementoByIdResult
        )

        val gottenInsertedMemento = mDataRepository.addMemento(mementoToInsert)

        Assert.assertEquals(getMementoByIdResult, insertMementoResult.get())
        Assert.assertEquals(mementoToInsert, gottenInsertedMemento)
    }

    @Test
    fun updateMementoTest() {
        val mementoToUpdate = DataMemento(1, "test memento")
        val updateMementoResult = AtomicReference<MementoEntity?>(null)
        val getMementoByIdResult = mementoToUpdate.toMementoEntity()

        initRepository(
            updateMementoResult = updateMementoResult,
            getMementoByIdResult = getMementoByIdResult
        )

        val gottenUpdatedMemento = mDataRepository.updateMemento(mementoToUpdate)

        Assert.assertEquals(getMementoByIdResult, updateMementoResult.get())
        Assert.assertEquals(mementoToUpdate, gottenUpdatedMemento)
    }

    @Test
    fun deleteMementoTest() {
        val mementoToDelete = DataMemento(1, "test memento")
        val deleteMementoByIdResult = AtomicReference<Long?>(null)

        initRepository(deleteMementoByIdResult = deleteMementoByIdResult)

        mDataRepository.deleteMementoById(mementoToDelete.id!!)

        Assert.assertEquals(mementoToDelete.id, deleteMementoByIdResult.get())
    }
}