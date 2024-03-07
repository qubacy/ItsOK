package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.model.operation

import com.qubacy.itsok.domain.settings.memento.model.Memento
import com.qubacy.itsok.ui.application.activity._common.screen._common.fragment.stateful.model.operation._common.UiOperation

class UpdateMementoUiOperation(
    val memento: Memento,
    val index: Int
) : UiOperation {

}