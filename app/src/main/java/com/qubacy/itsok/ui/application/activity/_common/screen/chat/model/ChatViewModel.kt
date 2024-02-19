package com.qubacy.itsok.ui.application.activity._common.screen.chat.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment._common.model.BaseViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class ChatViewModel @Inject constructor(

) : BaseViewModel() {

}

@Qualifier
annotation class ChatViewModelFactoryQualifier

class ChatViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (!modelClass.isAssignableFrom(ChatViewModel::class.java))
            throw IllegalArgumentException()

        return ChatViewModel() as T
    }
}

@Module
@InstallIn(ActivityRetainedComponent::class)
object ChatViewModelFactoryModule {
    @Provides
    @ChatViewModelFactoryQualifier
    fun provideChatViewModelFactory(

    ): ViewModelProvider.Factory {
        return ChatViewModelFactory()
    }
}