package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common

import androidx.core.graphics.Insets
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.qubacy.itsok.R
import com.qubacy.itsok._common.error.Error
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.BaseViewModel
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model._common.state.BaseUiState

abstract class BaseFragment<
    UiStateType : BaseUiState,
    ViewModelType : BaseViewModel<UiStateType>
>() : Fragment() {
    companion object {
        const val TAG = "BaseFragment"
    }

    protected abstract val mModel: ViewModelType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catchViewInsets(view)
    }

    override fun onStart() {
        super.onStart()

        processUiState(mModel.uiState)
    }

    protected open fun viewInsetsToCatch(): Int {
        return WindowInsetsCompat.Type.statusBars() or
               WindowInsetsCompat.Type.navigationBars()
    }

    private fun catchViewInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insetsRes: WindowInsetsCompat? ->
            val insets = insetsRes?.getInsets(viewInsetsToCatch())

            if (insets != null) adjustViewToInsets(insets)

            WindowInsetsCompat.CONSUMED
        }
    }

    protected open fun adjustViewToInsets(insets: Insets) { }

    protected open fun processUiState(uiState: UiStateType) {
        if (uiState.error != null) onErrorOccurred(uiState.error!!)
    }

    open fun onMessageOccurred(
        message: String,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    fun onMessageOccurred(
        @StringRes message: Int,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        onMessageOccurred(getString(message), duration)
    }

    protected open fun onErrorHandled() { }

    open fun onErrorOccurred(error: Error) {
        val onDismiss = Runnable {
            handleError(error)
            onErrorHandled()
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.component_error_dialog_title_text)
            .setMessage(error.message)
            .setNeutralButton(R.string.component_error_dialog_button_neutral_caption) { _, _ ->
                onDismiss.run()
            }
            .setOnDismissListener {
                onDismiss.run()
            }
            .show()
    }

    open fun handleError(error: Error) {
        if (error.isCritical) {
            requireActivity().finishAndRemoveTask()
        }
    }
}