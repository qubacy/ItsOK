package com.qubacy.itsok._common.error

data class Error(
    val id: Long,
    val message: String,
    val isCritical: Boolean
) {

}