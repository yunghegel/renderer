package org.yunghegel.gdx.renderer.deffered

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttribute
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.g3d.utils.RenderContext
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.utils.GdxRuntimeException
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute
import org.lwjgl.opengl.GL32C
import org.yunghegel.gdx.util.insertAt
import org.yunghegel.gdx.util.line

class GBufferShader(val config: DefaultShader.Config = Config(), val renderable: Renderable) : BaseShader() {
    var program: ShaderProgram? = null
    var camera: Camera? = null
    var context: RenderContext? = null
    var u_projTrans: Int = 0
    var u_worldTrans: Int = 0
    var prefix = ""

    init {

    }

    constructor(config: DefaultShader.Config, renderable: Renderable, prefix: String) : this(config, renderable) {
        this.prefix = prefix
    }

    override fun init() {
        prefix = generatePrefix(renderable)
        val vertexShader = prefix line config.vertexShader
        val fragmentShader = prefix line config.fragmentShader
        ShaderProgram.pedantic = false
        program = ShaderProgram(vertexShader, fragmentShader)



        if (!program!!.isCompiled) throw GdxRuntimeException(program!!.log)
        u_projTrans = program!!.getUniformLocation("u_projTrans")
        u_worldTrans = program!!.getUniformLocation("u_worldTrans")

        val handle = program!!.handle


        GL32C.glBindFragDataLocation(handle, 0, "g_position")
        GL32C.glBindFragDataLocation(handle, 1, "g_normal")
        GL32C.glBindFragDataLocation(handle, 2, "g_color")
        GL32C.glBindFragDataLocation(handle, 3, "g_metallicRoughness")
        GL32C.glBindFragDataLocation(handle, 4, "g_uv")
        GL32C.glBindFragDataLocation(handle, 5, "g_depth")

        Gdx.gl20.glLinkProgram(handle)

        println(this)
    }

    override fun dispose() {
        program!!.dispose()
    }

    override fun begin(camera: Camera, context: RenderContext) {
        this.camera = camera
        this.context = context
        program!!.bind()
        program!!.setUniformMatrix(u_projTrans, camera.combined)

        context.setDepthTest(GL20.GL_LEQUAL)
        context.setCullFace(GL20.GL_BACK)
    }

    override fun render(renderable: Renderable) {

        if (!canRender(renderable)) return

        val mat = renderable.material
        val color = mat.get(TextureAttribute.Diffuse) as TextureAttribute? ?: mat.get(PBRTextureAttribute.BaseColorTexture) as TextureAttribute?

        if (color != null) {
            program!!.setUniformi("u_colorTexture", context!!.textureBinder.bind(color.textureDescription.texture))
        }

        val normal = mat.get(TextureAttribute.Normal) as TextureAttribute? ?: mat.get(PBRTextureAttribute.NormalTexture) as TextureAttribute?
        if (normal != null) {
            program!!.setUniformi("u_normalTexture", context!!.textureBinder.bind(normal.textureDescription.texture))
        }

        if (mat.has(PBRTextureAttribute.MetallicRoughnessTexture) || mat.has(TextureAttribute.Specular)) {
            val metallicRoughness = mat.get(PBRTextureAttribute.MetallicRoughnessTexture) as TextureAttribute?
            if (metallicRoughness != null) {
                program!!.setUniformi("u_metallicRoughnessTexture", context!!.textureBinder.bind(metallicRoughness.textureDescription.texture))
            }
            val specular = mat.get(TextureAttribute.Specular) as TextureAttribute?
            if (specular != null) {
                program!!.setUniformi("u_specularTexture", context!!.textureBinder.bind(specular.textureDescription.texture))
            }
        }

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST)
        Gdx.gl.glEnable(GL20.GL_CULL_FACE)
        Gdx.gl.glDepthFunc(GL20.GL_LEQUAL)

        program!!.setUniformMatrix(u_worldTrans, renderable.worldTransform)
        renderable.meshPart.render(program)
    }


    override fun compareTo(other: Shader): Int {
        return 0
    }

    override fun canRender(instance: Renderable): Boolean {
        return (instance.material != null && instance.meshPart != null && instance.meshPart.mesh != null)
    }



    class Config : DefaultShader.Config() {
        var defaultVertexShaderPath = "shaders/geometry.vert"
        var defaultFragmentShaderPath = "shaders/geometry.frag"

        var version : String = "#version 330"
        var core = true
        init {
            vertexShader = GBufferShaderProvider.getDefaultVertexShader()
            fragmentShader = GBufferShaderProvider.getDefaultFragmentShader()
        }
    }

    override fun toString(): String {
        return "[VertexShader]\n${prefix line config.vertexShader}\n[FragmentShader]\n${prefix line config.fragmentShader}]"
    }

    companion object {
        fun generatePrefix(renderable: Renderable, config : DefaultShader.Config = Config()) : String {
            var prefix = ""
            if (config is Config) {
                prefix += config.version
                if (config.core) prefix += " core\n" else prefix += "\n"
            } else {
                prefix += "#version 140\n"
            }


            val mesh = renderable.meshPart.mesh
            val material = renderable.material

            val attrs = mesh.vertexAttributes
            if (attrs.contains(VertexAttribute.Normal())) {
                prefix += "#define NORMAL_ATTRIBUTE\n"
            }
            if (attrs.contains(VertexAttribute.TexCoords(0))) {
                prefix += "#define UV_ATTRIBUTE\n"
            }
            if (attrs.contains(VertexAttribute.ColorPacked()) or attrs.contains(VertexAttribute.ColorUnpacked())) {
                prefix += "#define COLOR_ATTRIBUTE\n"
            }
            if (attrs.contains(VertexAttribute.Tangent())) {
                prefix += "#define TANGENT_ATTRIBUTE\n"
            }
            if (attrs.contains(VertexAttribute.Binormal())) {
                prefix += "#define BINORMAL_ATTRIBUTE\n"
            }
            if (material.has(TextureAttribute.Diffuse) or material.has(PBRTextureAttribute.BaseColorTexture)) {
                prefix += "#define COLOR_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Normal) or material.has(PBRTextureAttribute.NormalTexture)) {
                prefix += "#define NORMAL_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Specular)) {
                prefix += "#define SPECULAR_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Emissive) or material.has(PBRTextureAttribute.EmissiveTexture)) {
                prefix += "#define EMISSIVE_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Reflection)) {
                prefix += "#define REFLECTION_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Ambient)) {
                prefix += "#define AMBIENT_TEXTURE\n"
            }
            if (material.has(TextureAttribute.Bump) or material.has(PBRTextureAttribute.Bump)) {
                prefix += "#define BUMP_TEXTURE\n"
            }
            if (material.has(PBRTextureAttribute.MetallicRoughnessTexture)) {
                prefix += "#define METALLIC_ROUGHNESS_TEXTURE\n"
            }
            if (material.has(PBRTextureAttribute.OcclusionTexture)) {
                prefix += "#define OCCLUSION_TEXTURE\n"
            }

            return prefix
        }
    }
}
