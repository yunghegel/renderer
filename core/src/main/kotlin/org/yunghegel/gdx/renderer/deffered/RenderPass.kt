package org.yunghegel.gdx.renderer.deffered

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Renderable
import ktx.collections.GdxArray
import org.yunghegel.gdx.renderer.DeferredRenderer
import org.yunghegel.gdx.renderer.ForwardRenderer

abstract class RenderPass() {

    val renderables : GdxArray<Renderable> = GdxArray()




    abstract fun render(objects: GdxArray<ModelInstance>, camera: Camera, environment: Environment)

    abstract fun resize(width: Int, height: Int)

    abstract fun dispose()




}