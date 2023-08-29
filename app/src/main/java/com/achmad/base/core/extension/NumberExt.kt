package com.achmad.base.core.extension

fun Number?.orZero(): Number {
    return this ?: 0
}
