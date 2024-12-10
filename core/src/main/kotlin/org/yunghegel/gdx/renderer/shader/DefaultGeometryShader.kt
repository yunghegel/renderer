package org.yunghegel.gdx.renderer.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.Attribute
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.glutils.ShaderProgram

class DefaultGeometryShader(val renderable: Renderable?) : DefaultShader(renderable){

    companion object {
        const val DEFAULT_VERTEX_PATH = "shaders/geometry.vert"
        const val DEFAULT_FRAGMENT_PATH = "shaders/geometry.frag"
    }

    init {
        initShader(DEFAULT_VERTEX_PATH, DEFAULT_FRAGMENT_PATH)
        init(program,renderable)
    }



    var program : ShaderProgram? = null
    var context : RenderContext? = null
    var handle : Int = 0







    fun initShader(vertexPath: String, fragmentPath: String) {

        checkAttribute(TextureAttribute.Diffuse, renderable!!, "DIFFUSE_TEXTURE")

        this.program = ShaderProgram(
            Gdx.files.internal(vertexPath),
            Gdx.files.internal(fragmentPath)
        )

        if (!program!!.isCompiled) {
            throw IllegalStateException("Shader compilation failed: ${program!!.log}")
        }

        handle = program.handle
    }

    fun checkAttribute(attribute: Long, renderable: Renderable, defineIf : String? = null) {
        val has = renderable.material.get(attribute)
        if (has != null) {
            if (defineIf != null) {
                ShaderProgram.prependFragmentCode = "#define $defineIf\n"
                println("Defined $defineIf")
            }
        }
    }
}