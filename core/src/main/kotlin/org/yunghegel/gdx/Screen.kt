package org.yunghegel.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader
import ktx.app.KtxScreen
import ktx.collections.GdxArray
import net.mgsx.gltf.scene3d.utils.MaterialConverter
import org.yunghegel.gdx.renderer.DeferredRenderer


class Screen : KtxScreen {

    val models : MutableList<Model> = mutableListOf()
    val renderer : DeferredRenderer = DeferredRenderer()
    val renderables : GdxArray<ModelInstance> = GdxArray()

    override fun dispose() {
        models.forEach { it.dispose() }
    }

    override fun show() {

        renderer.setupCamera()
        renderer.setupEnvironment()

        val model = ObjLoader().loadModel(Gdx.files.internal("models/suzanne.obj"))
        model.materials.forEach { MaterialConverter.makeCompatible(it) }
        val instance = ModelInstance(model)
        models.add(model)
        renderables.add(instance)
    }

    override fun render(delta: Float) {
        renderer.render(renderables, renderer.camera, renderer.environment)
    }

}

