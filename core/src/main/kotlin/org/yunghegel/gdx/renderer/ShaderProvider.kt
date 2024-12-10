package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider
import org.yunghegel.gdx.renderer.shader.DefaultGeometryShader

class ShaderProvider : DefaultShaderProvider() {

    override fun getShader(renderable: Renderable?): Shader {
        return DefaultGeometryShader(renderable)
    }

    override fun createShader(renderable: Renderable?): Shader {
        return DefaultGeometryShader(renderable)
    }
}