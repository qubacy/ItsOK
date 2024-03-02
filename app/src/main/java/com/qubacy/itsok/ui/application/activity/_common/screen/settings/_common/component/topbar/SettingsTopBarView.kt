package com.qubacy.itsok.ui.application.activity._common.screen.settings._common.component.topbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import com.google.android.material.appbar.AppBarLayout
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.ComponentSettingsTopBarBinding

class SettingsTopBarView(
    context: Context,
    attrs: AttributeSet
) : AppBarLayout(context, attrs) {
    companion object {
        val DEFAULT_NAVIGATION_BUTTON_ICON_DRAWABLE_RES_ID = R.drawable.ic_go_back
    }

    private lateinit var mBinding: ComponentSettingsTopBarBinding

    var title: String
        get() = mBinding.componentSettingsTopBar.title.toString()
        set(value) { mBinding.componentSettingsTopBar.title = value }

    init {
        inflate()
        initWithAttrs(attrs)
        initComponents()
    }

    private fun inflate() {
        val layoutInflater = LayoutInflater.from(context)

        mBinding = ComponentSettingsTopBarBinding.inflate(layoutInflater, this)
    }

    private fun initWithAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SettingsTopBarView, 0, 0
        ).apply {
            try {
                val title = getString(R.styleable.SettingsTopBarView_settingsTopBarTitle)
                val navigationIcon = getDrawable(
                    R.styleable.SettingsTopBarView_settingsTopBarNavigationButtonIcon
                ) ?: loadDefaultNavigationIconDrawable()

                mBinding.componentSettingsTopBar.title = title
                mBinding.componentSettingsTopBar.navigationIcon = navigationIcon

            }
            finally { recycle() }
        }
    }

    private fun loadDefaultNavigationIconDrawable(): Drawable {
        return ResourcesCompat.getDrawable(
            resources,
            DEFAULT_NAVIGATION_BUTTON_ICON_DRAWABLE_RES_ID,
            context.theme
        )!!
    }

    private fun initComponents() {
        mBinding.componentSettingsTopBar.setNavigationOnClickListener {
            Navigation.findNavController(this).navigateUp()
        }
    }
}