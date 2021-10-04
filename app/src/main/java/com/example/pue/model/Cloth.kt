package com.example.pue.model

import androidx.annotation.DrawableRes

data class Cloth(
    @DrawableRes
    val imageResourceId: Int,
    val name: String,
    val order: Int,
    var checked: Boolean
)