package com.oliviermarteaux.shared.ui

import android.R.attr.description
import android.R.attr.onClick
import android.R.attr.text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.semantics.Role
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class WidgetType(val title: String){
    EDIT_BOX("Edit Box")
}

fun semanticsContentDescription(
    widgetType: WidgetType? = null,
    contentDescription: String? = null,
    state: Boolean? = null,
    trueStateDescription: String = "",
    falseStateDescription: String = "",
    onClickLabel: String? = null,
    onLongClickLabel: String? = null,
    text: String = "",
): String {

    val widgetType: String = widgetType?.title?.let{"$it. "}?:""
    val contentDescription: String = contentDescription?.let{"$it. "}?:""
    val stateDescription: String = state?.let{
        if (it) "$trueStateDescription. $text. " else "$falseStateDescription. "
    }?:""
    val clickLabel: String = onClickLabel?.let{"Double tap to $it. "}?:""
    val longClickLabel = onLongClickLabel?.let{"Double tap and hold to $it. "}?:""

    val talkback: String = "$widgetType $contentDescription $stateDescription $clickLabel $longClickLabel"

    return talkback
}