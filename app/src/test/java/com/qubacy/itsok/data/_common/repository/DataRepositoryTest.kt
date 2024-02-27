package com.qubacy.itsok.data._common.repository

import com.qubacy.itsok.data._common.repository._common.DataRepository

abstract class DataRepositoryTest<DataRepositoryType : DataRepository> {
    protected lateinit var mDataRepository: DataRepositoryType


}