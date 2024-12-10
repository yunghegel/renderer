package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import ktx.collections.GdxArray

interface ForwardRenderer {


    fun render(objects: GdxArray<ModelInstance>, camera: Camera, environment: Environment)

    fun resize(width: Int, height: Int)

    fun dispose()

}