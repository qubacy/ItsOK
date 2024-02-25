package com.qubacy.itsok.data._common.repository

abstract class DataRepositoryTest<DataRepositoryType : DataRepository> {
    protected lateinit var mDataRepository: DataRepositoryType


}