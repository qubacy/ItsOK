package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.business.BusinessFragmentTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.PositiveMementoesViewModel
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.PositiveMementoesUiState
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.runner.RunWith
import com.qubacy.itsok.R
import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.domain.settings.memento.model._test.util.MementoUtilGenerator
import com.qubacy.itsok.ui._common._test.view.util.action.swipe.SwipeViewActionUtil
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional.setNavigationResult
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor._common.mode.MementoEditorMode
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.result.MementoEditorResult
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.module.PositiveMementoesViewModelModule
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.AddMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.SetMementoesUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation.UpdateMementoUiOperation
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.state.TestPositiveMementoesUiState
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import kotlin.math.min

@HiltAndroidTest
@UninstallModules(
    PositiveMementoesViewModelModule::class
)
@RunWith(AndroidJUnit4::class)
class PositiveMementoesFragmentTest : BusinessFragmentTest<
    PositiveMementoesUiState,
    TestPositiveMementoesUiState,
    PositiveMementoesViewModel,
    PositiveMementoesFragment
>() {
    @Before
    override fun setup() {
        super.setup()
    }

    override fun getFragmentClass(): Class<PositiveMementoesFragment> {
        return PositiveMementoesFragment::class.java
    }

    override fun getCurrentDestination(): Int {
        return R.id.positiveMementoesFragment
    }

    @Test
    fun processSetMementoesUiOperationOnNullListTest() = runTest {
        val expectedMementoes = MementoUtilGenerator.generateMementoes(2)
        val setMementoesUiOperation = SetMementoesUiOperation(expectedMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        assertMaxExpectedMementoesVisible(expectedMementoes)
    }

    @Test
    fun processSetMementoesUiOperationOnNotNullListTest() = runTest {
        val initMementoes = MementoUtilGenerator.generateMementoes(2)
        val initSetMementoesUiOperation = SetMementoesUiOperation(initMementoes)

        mUiOperationFlow.emit(initSetMementoesUiOperation)

        val expectedMementoes = MementoUtilGenerator.generateMementoes(2, initMementoes.size)
        val setMementoesUiOperation = SetMementoesUiOperation(expectedMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        assertMaxExpectedMementoesVisible(expectedMementoes)
    }

    @Test
    fun processAddMementoUiOperationOnEmptyListTest() = runTest {
        val expectedMemento = MementoUtilGenerator.generateMemento()
        val addMementoUiOperation = AddMementoUiOperation(expectedMemento)

        mUiOperationFlow.emit(addMementoUiOperation)

        Espresso.onView(withText(expectedMemento.text))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun processAddMementoUiOperationOnNotEmptyListTest() = runTest {
        val initMementoes = MementoUtilGenerator.generateMementoes(1)
        val setMementoesUiOperation = SetMementoesUiOperation(initMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        val expectedMemento = MementoUtilGenerator.generateMemento(id = initMementoes.size.toLong())
        val expectedMementoes = initMementoes.plus(expectedMemento)
        val addMementoUiOperation = AddMementoUiOperation(expectedMemento)

        mUiOperationFlow.emit(addMementoUiOperation)

        assertMaxExpectedMementoesVisible(expectedMementoes)
    }

    @Test
    fun processUpdateMementoUiOperationTest() = runTest {
        val initMementoes = MementoUtilGenerator.generateMementoes(2)
        val setMementoesUiOperation = SetMementoesUiOperation(initMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        val updatedMementoIndex = 0
        val updatedMemento = initMementoes[updatedMementoIndex].copy(text = "updated memento")
        val expectedMementoes = initMementoes.toMutableList()
            .apply { this[updatedMementoIndex] =  updatedMemento }
        val updateMementoUiOperation = UpdateMementoUiOperation(updatedMemento, updatedMementoIndex)

        mUiOperationFlow.emit(updateMementoUiOperation)

        assertMaxExpectedMementoesVisible(expectedMementoes)
    }

    @Test
    fun removeMementoByRightSwipeTest() = runTest {
        val initMementoes = MementoUtilGenerator.generateMementoes(2)
        val setMementoesUiOperation = SetMementoesUiOperation(initMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        val mementoToRemove = initMementoes.first()

        var mementoPreviewView: View? = null
        var mementoPreviewContainerView: View? = null

        mActivityScenario.onActivity {
            mementoPreviewView = it.findViewById<View>(
                R.id.component_positive_memento_preview_wrapper)
            mementoPreviewContainerView = it.findViewById<View>(
                R.id.fragment_positive_mementoes_list)
        }

        val mementoCenterY = mementoPreviewContainerView!!.y +
                mementoPreviewView!!.measuredHeight / 2

        val swipeEndX = mementoPreviewContainerView!!.width.toFloat()
        val swipeEndY = mementoCenterY

        Espresso.onView(withText(mementoToRemove.text))
            .perform(
                SwipeViewActionUtil.generateSwipeViewAction(
                    swipeEndX, swipeEndY, GeneralLocation.CENTER_LEFT),
                WaitViewAction(500)
            ).check(ViewAssertions.doesNotExist())

        val testUiState = getTestUiState()

        Assert.assertEquals(mementoToRemove.id, testUiState.mementoToRemoveId)
    }

    @Test
    fun processMementoEditorResultInCreatingModeTest() {
        navigateToMementoEditor()

        val editorMode = MementoEditorMode.CREATOR
        val expectedMemento = MementoUtilGenerator.generateMemento()
        val editorResult = MementoEditorResult(editorMode, expectedMemento)

        mActivityScenario.onActivity {
            mFragment.setNavigationResult(editorResult)
        }

        val testUiState = getTestUiState()
        val gottenMemento = testUiState.mementoToCreate

        Assert.assertEquals(expectedMemento, gottenMemento)
    }

    @Test
    fun processMementoEditorResultInEditingModeTest() {
        navigateToMementoEditor()

        val editorMode = MementoEditorMode.EDITOR
        val expectedMemento = MementoUtilGenerator.generateMemento()
        val editorResult = MementoEditorResult(editorMode, expectedMemento)

        mActivityScenario.onActivity {
            mFragment.setNavigationResult(editorResult)
        }

        val testUiState = getTestUiState()
        val gottenMemento = testUiState.mementoToUpdate

        Assert.assertEquals(expectedMemento, gottenMemento)
    }

    @Test
    fun clickingOnMementoPreviewLeadsToTransitionToEditorInEditingModeTest() = runTest {
        val initMementoes = MementoUtilGenerator.generateMementoes(1)
        val clickableMemento = initMementoes.first()
        val setMementoesUiOperation = SetMementoesUiOperation(initMementoes)

        mUiOperationFlow.emit(setMementoesUiOperation)

        Espresso.onView(withText(clickableMemento.text)).perform(ViewActions.click())

        val curDestination = mNavController.currentDestination?.id
        val curDestinationArgs = getCurrentDestinationNavArgs()
        val mode = curDestinationArgs?.get("mode") as MementoEditorMode

        Assert.assertEquals(R.id.mementoEditorDialogFragment, curDestination)
        Assert.assertEquals(MementoEditorMode.EDITOR, mode)
    }

    @Test
    fun clickingOnComposeButtonLeadsToTransitionToEditorInCreatingModeTest() {
        Espresso.onView(withId(R.id.fragment_positive_mementoes_button_compose_memento))
            .perform(ViewActions.click())

        val curDestination = mNavController.currentDestination?.id
        val curDestinationArgs = getCurrentDestinationNavArgs()
        val mode = curDestinationArgs?.get("mode") as MementoEditorMode

        Assert.assertEquals(R.id.mementoEditorDialogFragment, curDestination)
        Assert.assertEquals(MementoEditorMode.CREATOR, mode)
    }

    private fun navigateToMementoEditor() {
        mActivityScenario.onActivity {
            val action = PositiveMementoesFragmentDirections
                .actionPositiveMementoesFragmentToMementoEditorDialogFragment()

            mNavController.navigate(action)
        }
    }

    private fun assertMaxExpectedMementoesVisible(
        expectedMementoes: List<Memento>
    ) {
        val maxVisiblePreviewCount = getMaxVisiblePreviewItemCount()
        val expectedVisiblePreviewCount = min(maxVisiblePreviewCount, expectedMementoes.size)

        for (i in 0 until expectedVisiblePreviewCount) {
            val expectedMemento = expectedMementoes[i]

            Espresso.onView(withText(expectedMemento.text))
                .check(ViewAssertions.matches(isDisplayed()))
        }
    }

    private fun getMaxVisiblePreviewItemCount(): Int {
        var previewWrapperHeight = 0
        var previewContainerHeight = 0

        mActivityScenario.onActivity {
            val previewWrapper = it.findViewById<View>(
                R.id.component_positive_memento_preview_wrapper)
            val previewContainer = it.findViewById<View>(R.id.fragment_positive_mementoes_list)

            previewWrapperHeight = previewWrapper.measuredHeight
            previewContainerHeight = previewContainer.measuredHeight
        }

        return previewContainerHeight / previewWrapperHeight
    }
}