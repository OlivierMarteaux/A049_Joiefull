package com.oliviermarteaux.a049_joiefull.domain.model

import android.content.Context
import androidx.annotation.StringRes
import com.oliviermarteaux.a049_joiefull.R

enum class ItemCategory(@StringRes val titleRes: Int) {
    ACCESSORIES(R.string.category_accessories),
    BOTTOMS(R.string.category_bottoms),
    SHOES(R.string.category_shoes),
    TOPS(R.string.category_tops);

    fun getTitle(context: Context): String = context.getString(titleRes)
}