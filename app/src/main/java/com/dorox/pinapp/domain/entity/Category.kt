package com.dorox.pinapp.domain.entity

import android.text.style.IconMarginSpan
import androidx.compose.ui.graphics.vector.ImageVector
import com.dorox.pinapp.ui.icons.ic_pets


enum class Category(val text: String, val icon: ImageVector? = null) {

    NOT_SELECTED(text = ""),
    ALL("Все"),
    THINGS("Личные вещи"),
    PETS("Животные", icon = ic_pets),
    ELECTRONICS("Электроника"),
    DOCUMENTS("Документы"),
    OTHER("Другое"),

}