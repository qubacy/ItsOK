package com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.extensional

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.permission.PermissionRunner
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.util.permission.PermissionRunnerCallback

fun BaseFragment.closeSoftKeyboard() {
    val inputMethodManager =
        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    inputMethodManager?.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun <FragmentType>BaseFragment.runPermissionCheck(

) where FragmentType : Fragment, FragmentType : PermissionRunnerCallback {
   PermissionRunner<FragmentType>(this as FragmentType).requestPermissions()
}