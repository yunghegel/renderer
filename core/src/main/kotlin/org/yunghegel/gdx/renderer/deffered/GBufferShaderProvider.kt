package org.yunghegel.gdx.renderer.deffered

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider

class GBufferShaderProvider(config: DefaultShader.Config) : DefaultShaderProvider(config) {

    fun createPrefix(renderable: Renderable,config : DefaultShader.Config) : String {
        return GBufferShader.generatePrefix(renderable,config)
    }

    override fun createShader(renderable: Renderable): Shader {
        val prefix = createPrefix(renderable,config)
        return GBufferShader(config,renderable,prefix)
    }
    override fun getShader(renderable: Renderable): Shader {
        return createShader(renderable)
    }

    companion object {
        fun getDefaultVertexShader() : String {
            return Gdx.files.internal("shaders/geometry.vert").readString()
        }

        fun getDefaultFragmentShader() : String {
            return Gdx.files.internal("shaders/geometry.frag").readString()
        }
    }

}
