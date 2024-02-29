package com.qubacy.itsok.ui.application.activity._common.screen.settings._common.component.topbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.navigation.Navigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.ComponentSettingsTopBarBinding

class SettingsTopBarView(
    context: Context,
    attrs: AttributeSet
) : AppBarLayout(context, attrs) {
    private lateinit var mBinding: ComponentSettingsTopBarBinding

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

                mBinding.componentSettingsTopBar.title = title

            }
            finally { recycle() }
        }
    }

    private fun initComponents() {
        mBinding.componentSettingsTopBar.setNavigationOnClickListener {
            Navigation.findNavController(this).navigateUp()
        }
    }
}