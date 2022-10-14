package ru.netology.nmedia.util

import java.text.DecimalFormat

object CountLikeShare {
    fun roundingCount(value: Long): String {
        var roundingCount: Double = 0.00
        var result: String = ""

        if (value < 1_000) {
            roundingCount = value.toDouble()
        } else if (value >= 1_000 && value < 1_000_000) {
            roundingCount = (value.toDouble() / 1_000)
        } else if (value >= 1_000_000) {
            roundingCount = (value.toDouble() / 1_000_000)
        }
        when (value) {
            in 0 until 1000 -> result = roundingCount.toLong().toString()
            in 1_000 until 1_000_000 -> result =
                DecimalFormat("#0.0").format(roundingCount).plus("k")
            else -> result = DecimalFormat("#0.0").format(roundingCount).plus("m")
        }
        return result
    }
}