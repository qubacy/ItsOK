package com.qubacy.itsok.data.memento.repository.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.data._common.repository.source_common.local.database.LocalDatabaseDataSourceTest
import com.qubacy.itsok.data.memento.repository.source.local.entity.MementoEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalMementoDataSourceTest : LocalDatabaseDataSourceTest() {
    private lateinit var mLocalMementoDataSource: LocalMementoDataSource

    @Before
    override fun setup() {
        super.setup()

        mLocalMementoDataSource = mDatabase.mementoDao()
    }

    @Test
    fun insertMementoTest() {
        val expectedMementoEntity = MementoEntity(text = "test memento")

        mLocalMementoDataSource.insertMemento(expectedMementoEntity)

        val gottenMementoes = mLocalMementoDataSource.getMementoes()

        Assert.assertNotNull(gottenMementoes.find { it.text == expectedMementoEntity.text })
    }

    @Test
    fun getMementoByIdTest() {
        val expectedMementoEntity = MementoEntity(id = 1, text = "test memento")

        mLocalMementoDataSource.insertMemento(expectedMementoEntity)

        val gottenMemento = mLocalMementoDataSource.getMementoById(expectedMementoEntity.id)

        Assert.assertEquals(expectedMementoEntity, gottenMemento)
    }

    @Test
    fun getMementoesTest() {
        val expectedMementoEntities = listOf(
            MementoEntity(id = 1, text = "test memento 1"),
            MementoEntity(id = 2, text = "test memento 2")
        )

        for (expectedMementoEntity in expectedMementoEntities)
            mLocalMementoDataSource.insertMemento(expectedMementoEntity)

        val gottenMementoes = mLocalMementoDataSource.getMementoes()

        Assert.assertEquals(expectedMementoEntities.size, gottenMementoes.size)

        for (expectedMementoEntity in expectedMementoEntities)
            Assert.assertTrue(gottenMementoes.contains(expectedMementoEntity))
    }

    @Test
    fun updateMementoTest() {
        val originalMementoEntity = MementoEntity(id = 1, text = "test memento original")

        mLocalMementoDataSource.insertMemento(originalMementoEntity)

        val updatedMementoEntity = originalMementoEntity.copy(text = "test memento updated")

        mLocalMementoDataSource.updateMemento(updatedMementoEntity)

        val gottenMemento = mLocalMementoDataSource.getMementoById(originalMementoEntity.id)

        Assert.assertEquals(updatedMementoEntity, gottenMemento)
    }

    @Test
    fun deleteMementoTest() {
        val expectedMementoEntity = MementoEntity(id = 1, text = "test memento")

        mLocalMementoDataSource.insertMemento(expectedMementoEntity)
        mLocalMementoDataSource.deleteMementoById(expectedMementoEntity.id)

        val gottenMemento = mLocalMementoDataSource.getMementoById(expectedMementoEntity.id)

        Assert.assertNull(gottenMemento)
    }
}