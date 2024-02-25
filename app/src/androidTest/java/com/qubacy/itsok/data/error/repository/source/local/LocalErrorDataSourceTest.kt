package com.qubacy.itsok.data.error.repository.source.local

import android.content.ContentValues
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.data._common.repository.source_common.local.database.LocalDatabaseDataSourceTest
import com.qubacy.itsok.data._common.repository.source_common.local.database._common._test.insertable.LocalInsertableDatabaseDataSourceTest
import com.qubacy.itsok.data.error.repository.source.local.model.ErrorEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalErrorDataSourceTest(

) : LocalDatabaseDataSourceTest(), LocalInsertableDatabaseDataSourceTest<ErrorEntity> {
    private lateinit var mLocalErrorDataSource: LocalErrorDataSource

    @Before
    override fun setup() {
        super.setup()

        mLocalErrorDataSource = mDatabase.errorDao()
    }

    override fun packEntityContent(itemEntity: ErrorEntity): ContentValues {
        val contentValues = ContentValues().apply {
            put(ErrorEntity.ID_PROP_NAME, itemEntity.id)
            put(ErrorEntity.LANG_PROP_NAME, itemEntity.lang)
            put(ErrorEntity.MESSAGE_PROP_NAME, itemEntity.message)
            put(ErrorEntity.IS_CRITICAL_PROP_NAME, itemEntity.isCritical)
        }

        return contentValues
    }

    @Test
    fun getErrorByIdTest() {
        val expectedErrorEntity = ErrorEntity(0, "en", "test error", false)

        insertItems(mDatabase, ErrorEntity.TABLE_NAME, listOf(expectedErrorEntity))

        val gottenError = mLocalErrorDataSource.getErrorById(
            expectedErrorEntity.id, expectedErrorEntity.lang)

        Assert.assertEquals(expectedErrorEntity, gottenError)
    }
}