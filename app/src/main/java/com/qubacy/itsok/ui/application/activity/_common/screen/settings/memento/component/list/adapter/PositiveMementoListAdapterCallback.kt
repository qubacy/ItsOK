package com.qubacy.itsok.ui.application.activity._common.screen.settings.memento.component.list.adapter

interface PositiveMementoListAdapterCallback {
    fun onMementoClicked(id: Long)
    fun onMementoRemoved(id: Long)
}