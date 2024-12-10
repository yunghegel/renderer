package org.yunghegel.gdx.renderer.deffered

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer
import com.badlogic.gdx.utils.Disposable
import org.yunghegel.gdx.renderer.deffered.TextureAttachment.Companion.getTexture

class GBuffer() : Disposable {

    var width: Int = Gdx.graphics.height
    var height: Int = Gdx.graphics.height

    var attachments = TextureAttachment.entries

    var fbo : FrameBuffer = createFrameBuffer(width,height)

    fun getTexture(attachment: TextureAttachment): Texture? {
        return fbo.getTexture(attachment)
    }

    fun begin() {
        fbo.begin()
        Gdx.gl.glViewport(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
    }

    fun end() {
        fbo.end()
    }

    fun createFrameBuffer(w : Int, h: Int) : FrameBuffer {
        width = if (w <= 0) Gdx.graphics.width else w
        height = if (h <= 0) Gdx.graphics.height else h
        val builder = GLFrameBuffer.FrameBufferBuilder(width, height)
        attachments.forEach { attachment -> attachment.attach(builder) }
        return builder.build()
    }

    fun ensureFBO(w: Int,h : Int) : FrameBuffer {
        if (fbo.width != w || fbo.height != h) {
            fbo.dispose()
            fbo = createFrameBuffer(w,h)
        }
        return fbo
    }

    override fun dispose() {
        fbo.dispose()
    }




}
