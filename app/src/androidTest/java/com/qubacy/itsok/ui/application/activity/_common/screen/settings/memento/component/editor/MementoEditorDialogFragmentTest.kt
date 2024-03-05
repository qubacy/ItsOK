package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor

import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.base.BaseFragmentTest
import com.qubacy.itsok.R
import com.qubacy.itsok._common.context.util.getUriFromResId
import com.qubacy.itsok.domain.settings.memento.model._test.util.MementoUtilGenerator
import com.qubacy.itsok.ui._common._test.view.util.action.wait.WaitViewAction
import com.qubacy.itsok.ui._common._test.view.util.matcher.image.common.CommonImageViewMatcher
import com.qubacy.itsok.ui._common._test.view.util.matcher.toast.root.ToastRootMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional.getNavigationResult
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional.setNavigationResult
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.PositiveMementoesFragmentDirections
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor._common.mode.MementoEditorMode
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.data.MementoEditData
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.data.toMemento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.result.MementoEditorResult
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class MementoEditorDialogFragmentTest(

) : BaseFragmentTest<MementoEditorDialogFragment>() {
    private var mArgs: MementoEditorDialogFragmentArgs = MementoEditorDialogFragmentArgs()

    override fun getFragmentClass(): Class<MementoEditorDialogFragment> {
        return MementoEditorDialogFragment::class.java
    }

    override fun getCurrentDestination(): Int {
        return R.id.mementoEditorDialogFragment
    }

    override fun getFragmentArgs(): Bundle? {
        return mArgs.toBundle()
    }

    override fun initNavControllerDestination(navController: TestNavHostController) {
        navController.setCurrentDestination(R.id.positiveMementoesFragment)

        val action = PositiveMementoesFragmentDirections
            .actionPositiveMementoesFragmentToMementoEditorDialogFragment()

        navController.navigate(action)
    }

    override fun setup() {

    }

    @Test
    fun showingEditorTitleOnLaunchingInEditorModeTest() {
        val memento = MementoUtilGenerator.generateMemento()

        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.EDITOR, memento = memento)

        init()

        Espresso.onView(withText(R.string.component_memento_editor_title_editor_mode))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun showingCreatorTitleOnLaunchingInCreatorModeTest() {
        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.CREATOR)

        init()

        Espresso.onView(withText(R.string.component_memento_editor_title_creator_mode))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    override fun showMessageTest() {
        init()

        super.showMessageTest()
    }

    @Test
    fun showingMementoContentOnLaunchingInEditorModeTest() {
        val memento = MementoUtilGenerator.generateMemento(withImage = true)
        val mementoDrawable = InstrumentationRegistry.getInstrumentation()
            .targetContext.getDrawable(MementoUtilGenerator.DEFAULT_IMAGE_RESOURCE_ID)!! // todo: a kind of dirty way;

        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.EDITOR, memento = memento)

        init()

        Espresso.onView(withText(memento.text)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(withId(R.id.component_memento_editor_image_preview))
            .check(ViewAssertions.matches(CommonImageViewMatcher(mementoDrawable)))
    }

    @Test
    fun imagePreviewDisappearingOnClickingRemoveImageButtonTest() {
        val memento = MementoUtilGenerator.generateMemento(withImage = true)
        val imagePreviewAnimationDuration = getImagePreviewAnimationDuration()

        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.EDITOR, memento = memento)

        init()

        Espresso.onView(withId(R.id.component_memento_editor_image_preview_button_remove))
            .perform(ViewActions.click())
        Espresso.onView(withId(R.id.component_memento_editor_image_preview))
            .perform(WaitViewAction(imagePreviewAnimationDuration))
            .check(ViewAssertions.matches(ViewMatchers
                .withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun changingImageUriLeadsToChangingImagePreviewTest() {
        val memento = MementoUtilGenerator.generateMemento(withImage = true)
        val imagePreviewAnimationDuration = getImagePreviewAnimationDuration()
        val newMementoImageUri = InstrumentationRegistry.getInstrumentation().targetContext
            .getUriFromResId(R.drawable.ic_launcher_background)
        val newMementoImage = InstrumentationRegistry.getInstrumentation().targetContext
            .getDrawable(R.drawable.ic_launcher_background)!!

        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.EDITOR, memento = memento)

        init()

        mActivityScenario.onActivity {
            changeMementoImageUri(newMementoImageUri)
        }

        Espresso.onView(withId(R.id.component_memento_editor_image_preview))
            .perform(WaitViewAction(imagePreviewAnimationDuration))
            .check(ViewAssertions.matches(CommonImageViewMatcher(newMementoImage)))
    }

    @Test
    fun clickingSaveWithoutDataLeadsToShowingErrorTest() {
        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.CREATOR)

        init()

        Espresso.onView(withId(R.id.component_memento_editor_button_save))
            .perform(ViewActions.click())
        Espresso.onView(withText(R.string.fragment_memento_editor_dialog_error_invalid_memento))
            .inRoot(RootMatchers.withDecorView(ToastRootMatcher(mFragment.requireActivity())))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun clickingSaveWithDataLeadsToSettingFragmentResultAndNavigatingUpTest() {
        val expectedMode = MementoEditorMode.CREATOR
        val expectedMemento = MementoUtilGenerator.generateMemento()

        mArgs = MementoEditorDialogFragmentArgs(mode = expectedMode)

        init()

        // todo: there was a bug with unpredictably appearing capital letters during emulated typing;
        mActivityScenario.onActivity {
            it.findViewById<EditText>(R.id.component_memento_editor_text_input)
                .setText(expectedMemento.text)
        }

        Espresso.onView(withId(R.id.component_memento_editor_button_save))
            .perform(ViewActions.click())

        val curDestination = mNavController.currentDestination?.id
        var mementoEditorResult: MutableLiveData<MementoEditorResult?>? = null

        mActivityScenario.onActivity {
            mementoEditorResult = mFragment.getNavigationResult()
        }

        val gottenMode = mementoEditorResult!!.value!!.mode
        val gottenMemento = mementoEditorResult!!.value!!.memento

        Assert.assertEquals(R.id.positiveMementoesFragment, curDestination)
        Assert.assertEquals(expectedMode, gottenMode)
        Assert.assertEquals(expectedMemento, gottenMemento)
    }

    @Test
    fun clickingCancelLeadsToNavigatingUpTest() {
        mArgs = MementoEditorDialogFragmentArgs(mode = MementoEditorMode.CREATOR)

        init()

        Espresso.onView(withId(R.id.component_memento_editor_button_cancel))
            .perform(ViewActions.click())

        val curDestination = mNavController.currentDestination?.id

        Assert.assertEquals(R.id.positiveMementoesFragment, curDestination)
    }

    private fun getImagePreviewAnimationDuration(): Long {
        return InstrumentationRegistry.getInstrumentation().targetContext
            .resources.getInteger(
                R.integer.component_memento_editor_image_preview_visibility_change_animation_duration
            ).toLong()
    }

    private fun setMementoEditData(mementoEditData: MementoEditData) {
        MementoEditorDialogFragment::class.java
            .getDeclaredField("mMementoEditData")
            .apply { isAccessible = true }
            .set(mFragment, mementoEditData)
    }

    @MainThread
    private fun changeMementoImageUri(imageUri: Uri?) {
        MementoEditorDialogFragment::class.java
            .getDeclaredMethod("changeMementoImageUri", Uri::class.java)
            .apply { isAccessible = true }
            .invoke(mFragment, imageUri)
    }
}