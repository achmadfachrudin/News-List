package com.achmad.base.core.extension

import kotlinx.datetime.LocalDate

fun LocalDate.toStringWithFormat() {
    this.toEpochDays()
}
