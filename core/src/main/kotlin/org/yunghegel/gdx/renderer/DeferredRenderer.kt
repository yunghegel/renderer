package org.yunghegel.gdx.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.RenderableProvider
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.math.Vector3
import ktx.collections.GdxArray
import org.yunghegel.gdx.renderer.deffered.GBuffer
import org.yunghegel.gdx.renderer.pass.GeometryPass

class DeferredRenderer() : ForwardRenderer {

    private val gBuffer: GBuffer
    val renderContext : RenderContext
    val camera : Camera
    val environment : Environment

    val geometryPass = GeometryPass()


    init {
        gBuffer = GBuffer()
        renderContext = RenderContext(DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN))
        camera = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        environment = Environment()
    }

    override fun render(objects: GdxArray<ModelInstance>, camera: Camera, environment: Environment) {
        geometryPass.render(objects, camera, environment)
    }

    override fun resize(width: Int, height: Int) {
        gBuffer.ensureFBO(width, height)
    }

    override fun dispose() {
        gBuffer.dispose()
    }

    fun setupEnvironment() {
        environment.add(DirectionalLight().set(Color.WHITE, Vector3(-0.2f, -1f, 0.2f)))
        environment.set(ColorAttribute.createFog(Color.BLACK))
    }

    fun setupCamera() {
        camera.position.set(0f, 0f, 10f)
        camera.lookAt(0f, 0f, 0f)
        camera.near = 0.1f
        camera.far = 300f
        camera.update()
    }

}