package com.qubacy.itsok.data.error.repository.source.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.data._common.repository.source_common.local.database.LocalDatabaseDataSourceTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocalErrorDataSourceTest : LocalDatabaseDataSourceTest() {
    private lateinit var mLocalErrorDataSource: LocalErrorDataSource

    @Before
    override fun setup() {
        super.setup()

        mLocalErrorDataSource = mDatabase.errorDao()
    }

    @Test
    fun getErrorByIdTest() {
        // todo: implement the test once you have any errors in the DB..


    }
}