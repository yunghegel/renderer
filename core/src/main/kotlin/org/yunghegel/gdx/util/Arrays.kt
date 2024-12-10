package org.yunghegel.gdx.util


inline fun <reified T> insertAt(array: Array<T>, index: Int, value: T): Array<T> {
    val result = Array(array.size + 1) { if (it < index) array[it] else if (it == index) value else array[it - 1] }
    if (index >= array.size) result[array.size] = value
    return result
}