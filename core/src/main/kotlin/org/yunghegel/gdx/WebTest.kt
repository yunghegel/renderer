package org.yunghegel.gdx

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.TimeUtils.timeSinceMillis
import net.mgsx.gltf.loaders.gltf.GLTFLoader
import net.mgsx.gltf.scene3d.scene.SceneAsset
import org.yunghegel.gdx.renderer.deffered.GBuffer
import org.yunghegel.gdx.renderer.deffered.GBufferShader
import org.yunghegel.gdx.renderer.deffered.TextureAttachment
import org.yunghegel.gdx.util.Oscillator

class WebTest : ApplicationListener {
    var cam: PerspectiveCamera? = null
    var camController: CameraInputController? = null
    lateinit var shader: Shader
    lateinit var renderContext: RenderContext
    lateinit var model: Model
    lateinit var sceneModel : SceneAsset
    lateinit var bitmapFont: BitmapFont
    var renderable: Renderable? = null
    var batch: SpriteBatch? = null
    lateinit var gbuf : GBuffer
    lateinit var oscillator: Oscillator
    var textureIndex = 0

    private val position = Vector3(0f, 0f, 0f)

    override fun create() {
        cam = PerspectiveCamera(67f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        cam!!.position.set(20f, 12f, 20f)
        cam!!.lookAt(0f, 0f, 0f)
        cam!!.near = 0.1f
        cam!!.far = 300f
        cam!!.update()

        camController = CameraInputController(cam)
        Gdx.input.inputProcessor = camController

        val modelBuilder = ModelBuilder()
        sceneModel = GLTFLoader().load(Gdx.files.internal("models/teapot.gltf"))
        model = sceneModel.scene.model
        val blockPart = model.nodes[0].parts[0]

        renderable = Renderable()
        blockPart.setRenderable(renderable)
        renderable!!.environment = null
        renderable!!.worldTransform.idt()

        renderContext = RenderContext(DefaultTextureBinder(DefaultTextureBinder.ROUNDROBIN, 1))
        renderContext.apply {
            setDepthMask(true)
            setDepthTest(GL20.GL_LEQUAL)
            setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        }
        shader = GBufferShader(GBufferShader.Config(),renderable!!)
        shader.init()
        batch = SpriteBatch()
        gbuf = GBuffer()
        bitmapFont = BitmapFont().apply { data.scale(.1f)}
        oscillator = Oscillator(amplitude = 1f, speed = 2f)

    }

    var lastTime = 0L

    override fun render() {





        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
        camController!!.update()



        renderable?.let { obj ->
//            oscillator.update(position)
//            obj.worldTransform.setTranslation(position)
            obj.worldTransform.rotate(0f, 1f, 0f,  Gdx.graphics.deltaTime*3)
        }

        lastTime = timeSinceMillis(0)

        gbuf.begin()
        renderContext.begin()

        shader.begin(cam, renderContext)
        shader.render(renderable)
        shader.end()
        renderContext.end()
        gbuf.end()
        val tex = TextureRegion(gbuf.fbo.textureAttachments[textureIndex]).apply { flip(false, true) }
        batch!!.begin()
        batch!!.draw(tex, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())

        var screenXPercent = 0f
        var heightPortion = (Gdx.graphics.height / 4)

        TextureAttachment.entries.minus(TextureAttachment.Depth).forEach { attachment ->
            val tex = TextureRegion(gbuf.getTexture(attachment)).apply { flip(false, true) }
            batch!!.draw(tex, screenXPercent, 0f, Gdx.graphics.width.toFloat() / 4, heightPortion.toFloat())
            bitmapFont.draw(batch, attachment.name, screenXPercent + Gdx.graphics.width/12, heightPortion.toFloat())
            screenXPercent += Gdx.graphics.width / 5
        }

        bitmapFont.draw(batch, "glFrameBufferAttachmentSpec (layout $textureIndex)}", 0f, Gdx.graphics.height.toFloat() - 10f)




        handleInput()






        batch!!.end()
    }

    fun handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            textureIndex = 0
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            textureIndex = 1
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            textureIndex = 2
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            textureIndex = 3
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
           textureIndex = (textureIndex + 1) % 4
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            textureIndex = (textureIndex - 1 + 4) % 4

        }

    }

    override fun dispose() {
        shader.dispose()
        model!!.dispose()
    }

    override fun resize(width: Int, height: Int) {
        gbuf?.let { buf -> buf.ensureFBO(width, height) }
    }

    override fun pause() {
    }

    override fun resume() {
    }
}
