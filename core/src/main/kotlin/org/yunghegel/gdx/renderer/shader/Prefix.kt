package org.yunghegel.gdx.renderer.shader

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


class Prefix(var string: String = "") : ReadWriteProperty<Any,String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return string
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        string = value
    }

}
class xb{
    val x : String by prefix("")
}


fun prefix(value: String) : Prefix {
    return Prefix(value)
}
