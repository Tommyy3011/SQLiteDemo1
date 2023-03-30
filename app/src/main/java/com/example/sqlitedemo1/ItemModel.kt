package com.example.sqlitedemo1

import java.util.*

data class ItemModel(
    var id: Int = getAutoId(),
    var name: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(100)
        }
    }

}