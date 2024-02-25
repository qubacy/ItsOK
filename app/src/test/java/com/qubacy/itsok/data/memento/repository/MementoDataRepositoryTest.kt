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
        deleteMementoResult: AtomicReference<MementoEntity?> = AtomicReference<MementoEntity?>(null),
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
            }
        Mockito.`when`(localMementoDataSourceMock.updateMemento(AnyMockUtil.anyObject()))
            .thenAnswer {
                val memento = it.arguments[0] as MementoEntity

                updateMementoResult.set(memento)
            }
        Mockito.`when`(localMementoDataSourceMock.deleteMemento(AnyMockUtil.anyObject()))
            .thenAnswer {
                val memento = it.arguments[0] as MementoEntity

                deleteMementoResult.set(memento)
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

        initRepository(insertMementoResult = insertMementoResult)

        mDataRepository.addMemento(mementoToInsert)

        Assert.assertEquals(mementoToInsert.toMementoEntity(), insertMementoResult.get())
    }

    @Test
    fun updateMementoTest() {
        val mementoToUpdate = DataMemento(1, "test memento")
        val updateMementoResult = AtomicReference<MementoEntity?>(null)

        initRepository(updateMementoResult = updateMementoResult)

        mDataRepository.updateMemento(mementoToUpdate)

        Assert.assertEquals(mementoToUpdate.toMementoEntity(), updateMementoResult.get())
    }

    @Test
    fun deleteMementoTest() {
        val mementoToDelete = DataMemento(1, "test memento")
        val deleteMementoResult = AtomicReference<MementoEntity?>(null)

        initRepository(deleteMementoResult = deleteMementoResult)

        mDataRepository.deleteMemento(mementoToDelete)

        Assert.assertEquals(mementoToDelete.toMementoEntity(), deleteMementoResult.get())
    }
}