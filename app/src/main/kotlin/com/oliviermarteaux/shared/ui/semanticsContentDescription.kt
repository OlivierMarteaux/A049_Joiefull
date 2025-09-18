package com.oliviermarteaux.shared.ui

import android.R.attr.description
import androidx.compose.ui.semantics.Role

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
    val widgetType: String = widgetType?.title?.run{"$this. "}?:""
    val contentDescription: String = contentDescription?.run{"$contentDescription. "}?:""
    val stateDescription: String = state?.run{
        if (state) "$trueStateDescription. $text. " else "$falseStateDescription. "
    }?:""
    val clickLabel: String = onClickLabel?.run{"Double tap to $onClickLabel. "}?:""
    val longClickLabel = onLongClickLabel?.run{"Long tap to $onLongClickLabel. "}?:""
    return "$widgetType $contentDescription $stateDescription $clickLabel $longClickLabel"
}