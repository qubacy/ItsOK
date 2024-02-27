package com.qubacy.itsok.domain._common.usecase

import app.cash.turbine.test
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok._common.error.type._test.TestErrorType
import com.qubacy.itsok.data._common.repository._common.DataRepository
import com.qubacy.itsok.data.error.repository.ErrorDataRepository
import com.qubacy.itsok.domain._common.usecase._common.UseCase
import com.qubacy.itsok.domain._common.usecase._common.result.error.ErrorDomainResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
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

    @Test
    fun retrieveErrorTest() = runTest {
        val expectedErrorType = TestErrorType.TEST
        val expectedError = Error(expectedErrorType.id, "test error", false)

        mGetErrorResult = expectedError

        mUseCase.resultFlow.test {
            mUseCase.retrieveError(TestErrorType.TEST)

            val gottenResult = awaitItem()

            Assert.assertEquals(ErrorDomainResult::class, gottenResult::class)

            val gottenError = (gottenResult as ErrorDomainResult).error

            Assert.assertEquals(expectedError, gottenError)
        }
    }
}