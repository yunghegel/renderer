package org.yunghegel.gdx.renderer.pass

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.GL20.GL_DEPTH_BUFFER_BIT
import com.badlogic.gdx.graphics.g3d.*
import com.badlogic.gdx.graphics.g3d.utils.DefaultRenderableSorter
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.g3d.utils.TextureBinder
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.Pools
import ktx.collections.GdxArray
import org.yunghegel.gdx.renderer.DeferredRenderer
import org.yunghegel.gdx.renderer.ForwardRenderer
import org.yunghegel.gdx.renderer.RenderablePool
import org.yunghegel.gdx.renderer.ShaderProvider
import org.yunghegel.gdx.renderer.deffered.RenderPass

class GeometryPass() : RenderPass() {



    val sorter : DefaultRenderableSorter = object : DefaultRenderableSorter() {
        override fun sort(context: Camera?, renderables: GdxArray<Renderable>?) {
            renderables?.sort(compareBy { it.material.id })
        }
    }
    val program : ShaderProgram

    val context : RenderContext = RenderContext(DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN))

    val provider : DefaultShaderProvider = ShaderProvider()

    val pool : Pool<Renderable> = RenderablePool
    val batch : ModelBatch = ModelBatch(provider)
    val modelCache : ModelCache = ModelCache()

    var useModelCache : Boolean = true

    init {
        program = ShaderProgram(
            Gdx.files.internal("shaders/geometry.vert"),
            Gdx.files.internal("shaders/geometry.frag")
        )
        if (!program.isCompiled) {
            throw IllegalStateException("Shader compilation failed: ${program.log}")
        }

        program.bind()



    }

    override fun render(objects: GdxArray<ModelInstance>, camera: Camera, environment: Environment) {
        renderables.clear()
        if (useModelCache) {
            modelCache.begin()
            objects.forEach { instance ->
                modelCache.add(instance)
            }
            modelCache.end()
            modelCache.getRenderables(renderables, pool)
        } else {
            objects.forEach { instance ->
                instance.getRenderables(renderables, pool)
            }
        }


        with(Gdx.gl) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glClearColor(0f, 0f, 0f, 1f)
        }

        for (i in 0 until renderables.size) {
            val renderable = renderables[i]
            renderable.environment = environment
            val mesh = renderable.meshPart.mesh
            val material = renderable.material
            program.setUniformMatrix("u_projTrans", camera.view)
            program.setUniformMatrix("u_projTrans", camera.projection)
            program.setUniformMatrix("u_modelTrans", renderable.worldTransform)
            mesh.render(program, GL20.GL_TRIANGLES)
        }

    }

    override fun resize(width: Int, height: Int) {

    }

    override fun dispose() {

    }




}