package com.example.notes.test


fun Int.even(): Boolean =
    this % 2 == 0

fun Int.odd(): Boolean =
    this % 2 != 0


fun main() {
    val notes = listOf(1,2,3,4,5,6,7,8,9)

    println(notes
    )
}

fun <T> List<T>.splitToPairs(): List<Pair<T?, T?>> =
    toMutableList<T?>()
        .apply { if (this.size.odd()) this.add(null) }
        .zipWithNext()
        .filterIndexed { i, _ -> i.even() }

