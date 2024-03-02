package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.graphics.Insets
import androidx.core.os.BundleCompat
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.qubacy.itsok.R
import com.qubacy.itsok._common.util.context.checkUriValidity
import com.qubacy.itsok.databinding.ComponentMementoEditorBinding
import com.qubacy.itsok.ui.application.activity._common.MainActivity
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.BaseFragment
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor._common.mode.MementoEditorMode
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.data.MementoEditData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MementoEditorDialogFragment() : BaseFragment() {
    companion object {
        const val TAG = "MementoEditorDialogFragment"

        const val MEMENTO_EDIT_DATA_KEY = "mementoEditData"
    }

    private val mArgs: MementoEditorDialogFragmentArgs by navArgs()

    private lateinit var mBinding: ComponentMementoEditorBinding
    private var mImagePreviewVisibilityChangeAnimationDuration: Long = 0L

    private var mMementoEditData: MementoEditData = MementoEditData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initMementoDataWithMode(mArgs.mode)

        mImagePreviewVisibilityChangeAnimationDuration =
            resources.getInteger(R.integer
                .component_memento_editor_image_preview_visibility_change_animation_duration
            ).toLong()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState == null) return

        mMementoEditData = BundleCompat
            .getParcelable(savedInstanceState, MEMENTO_EDIT_DATA_KEY, MementoEditData::class.java)!!

        initInputsWithMementoData(mMementoEditData)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(MEMENTO_EDIT_DATA_KEY, mMementoEditData)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = ComponentMementoEditorBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDialogTitle()

        if (!mMementoEditData.isEmpty()) initInputsWithMementoData(mMementoEditData)

        mBinding.componentMementoEditorTopBar.setNavigationOnClickListener {
            onCloseDialogButtonClicked()
        }
        mBinding.componentMementoEditorTextInput.addTextChangedListener(afterTextChanged = {
            mMementoEditData.text = it?.toString()
        })
        mBinding.componentMementoEditorImageButtonUpload.setOnClickListener {
            onUploadImageButtonClicked()
        }
        mBinding.componentMementoEditorImagePreviewButtonRemove.setOnClickListener {
            onRemovePreviewImageButtonClicked()
        }
    }

    private fun initMementoDataWithMode(mode: MementoEditorMode) {
        when (mode) {
            MementoEditorMode.EDITOR -> initMementoDataForEditor()
            else -> {  }
        }
    }

    private fun initMementoDataForEditor() {
        val memento = mArgs.memento!!

        mMementoEditData.text = memento.text
        mMementoEditData.imageUri = memento.imageUri
    }

    private fun initInputsWithMementoData(mementoData: MementoEditData) {
        mBinding.componentMementoEditorTextInput.setText(mementoData.text)

        changeMementoImagePreviewUri(mementoData.imageUri)
    }

    private fun initDialogTitle() {
        val titleStringResId =
            when (mArgs.mode) {
                MementoEditorMode.CREATOR -> R.string.component_memento_editor_title_create_mode
                MementoEditorMode.EDITOR -> R.string.component_memento_editor_title_editor_mode
            }

        mBinding.componentMementoEditorTopBar.title = getString(titleStringResId)
    }

    private fun onCloseDialogButtonClicked() {
        dismiss()
    }

    private fun dismiss() {
        Navigation.findNavController(requireView()).navigateUp()
    }

    private fun onUploadImageButtonClicked() {
        (requireActivity() as MainActivity).pickPhoto {
            if (it == null) return@pickPhoto

            changeMementoImageUri(it)
        }
    }

    private fun onRemovePreviewImageButtonClicked() {
        if (mMementoEditData.imageUri == null) return

        changeMementoImageUri(null)
    }

    private fun changeMementoImageUri(imageUri: Uri?) {
        mMementoEditData.imageUri = imageUri

        changeMementoImagePreviewUri(imageUri)
    }

    private fun changeMementoImagePreviewUri(imageUri: Uri?) {
        val preparedImageUri = prepareImageUriBeforeShowing(imageUri)

        changeMementoImagePreviewVisibilityWithImage(
            preparedImageUri != null, preparedImageUri)
    }

    private fun changeMementoImagePreviewVisibilityWithImage(toVisible: Boolean, imageUri: Uri?) {
        animateMementoImagePreviewVisibilityChangeForImageView(imageUri, toVisible)
        animateMementoImagePreviewVisibilityChangeForRemoveButton(toVisible)
    }

    private fun animateMementoImagePreviewVisibilityChangeForImageView(
        imageUri: Uri?,
        toVisible: Boolean
    ) {
        val startScaleY = if (toVisible) 0f else 1f
        val endScaleY = if (toVisible) 1f else 0f

        val startAction = {
            if (toVisible) mBinding.componentMementoEditorImagePreview.setImageURI(imageUri)
        }
        val endAction = {
            if (!toVisible) {
                mBinding.componentMementoEditorImagePreview.visibility = View.GONE
                mBinding.componentMementoEditorImagePreview.setImageURI(imageUri)
            }
        }

        mBinding.componentMementoEditorImagePreview.apply {
            visibility = View.VISIBLE
            scaleY = startScaleY
            pivotY = 0f

        }.animate().apply {
            scaleY(endScaleY)

            interpolator = AccelerateInterpolator()
            duration = mImagePreviewVisibilityChangeAnimationDuration

        }.setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) { startAction() }

                override fun onAnimationCancel(animation: Animator) {
                    startAction()
                    endAction()
                }

                override fun onAnimationEnd(animation: Animator) { endAction() }
            }
        ).start()
    }

    private fun animateMementoImagePreviewVisibilityChangeForRemoveButton(
        toVisible: Boolean
    ) {
        val startAlpha = if (toVisible) 0f else 1f
        val endAlpha = if (toVisible) 1f else 0f

        val endAction = {
            if (!toVisible)
                mBinding.componentMementoEditorImagePreviewButtonRemove.visibility = View.GONE
        }

        mBinding.componentMementoEditorImagePreviewButtonRemove.apply {
            visibility = View.VISIBLE
            alpha = startAlpha

        }.animate().apply {
            alpha(endAlpha)

            interpolator = AccelerateInterpolator()
            duration = mImagePreviewVisibilityChangeAnimationDuration

        }.setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator) { endAction() }
                override fun onAnimationEnd(animation: Animator) { endAction() }
            }
        ).start()
    }

    private fun prepareImageUriBeforeShowing(imageUri: Uri?): Uri? {
        return if (imageUri != null)
            if (requireContext().checkUriValidity(imageUri)) imageUri else null
        else null
    }

    override fun adjustViewToInsets(insets: Insets) {
        super.adjustViewToInsets(insets)

        mBinding.componentMementoEditorTopBarWrapper.apply {
            updatePadding(top = insets.top)
        }
    }
}