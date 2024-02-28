package com.qubacy.itsok.domain._common.usecase

import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import org.junit.Before
import org.mockito.Mockito

abstract class UseCaseTest<UseCaseType : UseCase>() {
    protected lateinit var mUseCase: UseCaseType

    protected var mGetErrorResult: Error? = null

    @Before
    open fun setup() {
        init()
    }

    private fun init() {
        val repositories = initRepositories()

        initUseCase(repositories)
    }

    protected open fun initRepositories(): List<DataRepository> {
        val errorDataRepositoryMock = Mockito.mock(ErrorDataRepository::class.java)

        Mockito.`when`(errorDataRepositoryMock.getError(Mockito.anyLong()))
            .thenAnswer{ mGetErrorResult }

        return listOf(errorDataRepositoryMock)
    }

    protected abstract fun initUseCase(repositories: List<DataRepository>)
}