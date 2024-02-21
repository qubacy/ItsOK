package com.qubacy.itsok.ui.application.activity._common.screen.chat.component.typing.view.job

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TypingJobLauncher(
    private val mText: String,
    private val mTextView: TextView,
    private val mCharTypingDuration: Long,
    private val mEndAction: (() -> Unit)? = null
) {
    companion object {
        const val TAG = "TypingJobLauncher"
    }

    fun run(coroutineScope: CoroutineScope): Job {
        return coroutineScope.launch(Dispatchers.IO) {
            try {
                for (i in 1..mText.length) {
                    Log.d(
                        TAG, "run(): job = ${coroutineContext.job.toString()};" +
                            " text = ${mText.substring(0, 5)}; curLength = $i;")

                    withContext(Dispatchers.Main) {
                        coroutineContext.job.ensureActive()

                        mTextView.text = mText.substring(0, i)
                    }

                    delay(mCharTypingDuration)
                }
            }
            catch (_: CancellationException) { }
            catch (e: Exception) {
                e.printStackTrace()

                throw e
            }
            finally {
                coroutineScope.launch (Dispatchers.Main) {
                    mEndAction?.invoke()
                }
            }
        }
    }
}