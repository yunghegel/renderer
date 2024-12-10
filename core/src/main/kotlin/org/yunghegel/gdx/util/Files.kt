package org.yunghegel.gdx.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle

val String.internal : FileHandle get() = Gdx.files.internal(this)

val String.external : FileHandle get() = Gdx.files.external(this)

val String.local : FileHandle get() = Gdx.files.local(this)

val String.absolute : FileHandle get() = Gdx.files.absolute(this)

val String.classpath : FileHandle get() = Gdx.files.classpath(this)