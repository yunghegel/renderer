package org.yunghegel.gdx.util

const val LINE = "\n"

infix fun String.line(content: String) : String {
    return "$this\n$content"
}

fun String.prepend(content:String) : String {
    return "$content$this"
}

fun String.line() : String {
    return "$this\n"
}

fun String.append(content:String):String {
    return "$this$content"
}
