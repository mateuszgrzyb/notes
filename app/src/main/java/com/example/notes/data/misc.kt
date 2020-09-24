package com.example.notes.data


object GLOBAL {
    const val title = "title"
    const val body = "body"
    const val uuid = "uuid"
    const val note = "note"
}


fun <T> List<T>.splitToPairs(): List<Pair<T?, T?>> =
    this
        .toMutableList<T?>()
        .apply {
            if (size % 2 != 0) add(null)
        }
        .zipWithNext()
        .filterIndexed { i, _ -> i % 2 == 0}

fun main() {
    val a = listOf(0,1,2,3,4,5,6,7,8)
    val b = listOf(0,1,2,3,4,5,6,7,8,9)
    println(a.splitToPairs())
    println(b.splitToPairs())
}
