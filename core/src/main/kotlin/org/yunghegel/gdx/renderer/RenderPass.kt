package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance

abstract class RenderPass(val deferredRenderer: DeferredRenderer) {

    abstract fun render(objects: List<ModelInstance>, camera: Camera, environment: Environment)

    abstract fun resize(width: Int, height: Int)

    abstract fun dispose()


}