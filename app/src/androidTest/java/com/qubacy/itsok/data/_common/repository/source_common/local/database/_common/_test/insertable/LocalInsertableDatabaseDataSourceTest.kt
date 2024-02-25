package com.qubacy.itsok.data._common.repository.source_common.local.database._common._test.insertable

import android.content.ContentValues
import com.qubacy.itsok.data._common.repository.source.local.database.Database
import com.qubacy.itsok.data._common.repository.source_common.local.database._common._test.util.insert

interface LocalInsertableDatabaseDataSourceTest<ItemType> {
    fun insertItems(
        database: Database,
        tableName: String,
        items: List<ItemType>
    ) {
        for (item in items) {
            val contentValues = packEntityContent(item)

            database.insert(tableName, contentValues)
        }
    }

    fun packEntityContent(itemEntity: ItemType): ContentValues
}