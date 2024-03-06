package com.qubacy.itsok.ui.application.activity._common

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.qubacy.itsok.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"

        val PICK_PHOTO_MEDIA_REQUEST =
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    }

    private lateinit var mPickPhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    private var mPickPhotoCallback: ((Uri?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setOnExitAnimationListener {
                it.remove()

                enableEdgeToEdge() // todo: is it safe?
            }
        }

        setContentView(R.layout.activity_main)
        initPickPhotoLauncher()
    }

    private fun initPickPhotoLauncher() {
        mPickPhotoLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            mPickPhotoCallback?.invoke(it)
        }
    }

    fun pickPhoto(callback: (Uri?) -> Unit) {
        mPickPhotoCallback = callback

        mPickPhotoLauncher.launch(PICK_PHOTO_MEDIA_REQUEST)
    }
}