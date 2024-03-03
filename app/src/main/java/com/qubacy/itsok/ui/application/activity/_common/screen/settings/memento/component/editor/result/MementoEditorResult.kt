package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor.result

import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.editor._common.mode.MementoEditorMode
import java.io.Serializable

data class MementoEditorResult(
    val mode: MementoEditorMode,
    val memento: Memento
) : Serializable {

}