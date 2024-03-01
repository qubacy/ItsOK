package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.preview

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.qubacy.itsok.ui._common._test.view.util.matcher.image.common.CommonImageViewMatcher
import com.qubacy.itsok.ui.application.activity._common.screen._common.component._common.ViewTest
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento._common.model._test.util.UIMementoUtilGenerator
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class PositiveMementoPreviewViewTest(

) : ViewTest<PositiveMementoPreviewView>() {
    override fun createView(context: Context): PositiveMementoPreviewView {
        context.theme

        return PositiveMementoPreviewView(context)
    }

    @Test
    fun setMementoWithTextTest() {
        val expectedMemento = UIMementoUtilGenerator.generateUIMemento(withText = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMemento)
        }

        Espresso.onView(withText(expectedMemento.text))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun setMementoWithImageTest() {
        val expectedMemento = UIMementoUtilGenerator.generateUIMemento(withImage = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMemento)
        }

        Espresso.onView(isAssignableFrom(TextView::class.java))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(CommonImageViewMatcher(expectedMemento.image!!))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun setMementoWithTextAndImageTest() {
        val expectedMemento = UIMementoUtilGenerator
            .generateUIMemento(withText = true, withImage = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMemento)
        }

        Espresso.onView(withText(expectedMemento.text))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(CommonImageViewMatcher(expectedMemento.image!!))
            .check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun setMementoWithImageThenSetMementoWithTextTest() {
        val expectedMementoWithImage = UIMementoUtilGenerator.generateUIMemento(withImage = true)
        val expectedMementoWithText = UIMementoUtilGenerator.generateUIMemento(withText = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithImage)
        }

        Espresso.onView(isAssignableFrom(TextView::class.java))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(CommonImageViewMatcher(expectedMementoWithImage.image!!))
            .check(ViewAssertions.matches(isDisplayed()))

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithText)
        }

        Espresso.onView(withText(expectedMementoWithText.text))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun setMementoWithTextThenSetMementoWithImageTest() {
        val expectedMementoWithText = UIMementoUtilGenerator.generateUIMemento(withText = true)
        val expectedMementoWithImage = UIMementoUtilGenerator.generateUIMemento(withImage = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithText)
        }

        Espresso.onView(isAssignableFrom(ImageView::class.java))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(withText(expectedMementoWithText.text))
            .check(ViewAssertions.matches(isDisplayed()))

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithImage)
        }

        Espresso.onView(CommonImageViewMatcher(expectedMementoWithImage.image!!))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(isAssignableFrom(TextView::class.java))
            .check(ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun setMementoWithImageThenSetMementoWithImageAndTextTest() {
        val expectedMementoWithImage = UIMementoUtilGenerator.generateUIMemento(withImage = true)
        val expectedMementoWithTextAndImage = UIMementoUtilGenerator
            .generateUIMemento(withText = true, withImage = true)

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithImage)
        }

        Espresso.onView(isAssignableFrom(TextView::class.java))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(CommonImageViewMatcher(expectedMementoWithImage.image!!))
            .check(ViewAssertions.matches(isDisplayed()))

        mActivityScenarioRule.scenario.onActivity {
            mView.setMemento(expectedMementoWithTextAndImage)
        }

        Espresso.onView(withText(expectedMementoWithTextAndImage.text))
            .check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(CommonImageViewMatcher(expectedMementoWithTextAndImage.image!!))
            .check(ViewAssertions.matches(isDisplayed()))
    }
}