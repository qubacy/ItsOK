package com.qubacy.itsok.ui.application.activity._common.screen.settings.general.component.setting

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.button.MaterialButton
import com.qubacy.itsok.R
import com.qubacy.itsok.databinding.ComponentSettingBinding

class SettingView(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {
    private lateinit var mBinding: ComponentSettingBinding

    init {
        inflate()
        initWithAttrs(attrs)
    }

    private fun inflate() {
        val layoutInflater = LayoutInflater.from(context)

        mBinding = ComponentSettingBinding.inflate(layoutInflater, this)
    }

    private fun initWithAttrs(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.SettingView, 0, 0
        ).apply {
            try {
                val iconImage = getDrawable(R.styleable.SettingView_settingIcon)
                val title = getString(R.styleable.SettingView_settingTitle)
                val buttonIcon = getDrawable(R.styleable.SettingView_settingButtonIcon)

                mBinding.componentSettingIcon.setImageDrawable(iconImage)
                mBinding.componentSettingText.text = title
                mBinding.componentSettingButton.icon = buttonIcon

            }
            finally { recycle() }
        }
    }

    fun getButton(): MaterialButton {
        return mBinding.componentSettingButton
    }
}