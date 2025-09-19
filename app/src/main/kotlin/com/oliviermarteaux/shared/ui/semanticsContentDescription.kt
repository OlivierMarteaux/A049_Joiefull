package com.oliviermarteaux.shared.ui

import android.R.attr.description
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

fun semanticsContentDescription(
    contentDescription: String? = null,
    state: Boolean? = null,
    trueStateDescription: String = "",
    falseStateDescription: String = "",
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    text: String = "",
): String {

    val contentDescription: String = contentDescription?.let{"$it. "}?:""
    val stateDescription: String = state?.let{
        if (it) "$trueStateDescription. $text. " else "$falseStateDescription. "
    }?:""
    val clickLabel: String = onClickLabel ?:""
    val longClickLabel = onLongClickLabel ?:""

    val talkback: String = "$contentDescription $stateDescription $clickLabel $longClickLabel"

    return talkback
}