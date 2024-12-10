package org.yunghegel.gdx.renderer.deffered

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer

enum class TextureAttachment(val layout : Int,val internalFormat : Int, val format : Int, val type : Int) {

    Position(0, GL30.GL_RGBA8, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE),
    Normal(1, GL30.GL_RGB8, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE),
    Color(2, GL30.GL_RGB8, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE),
    MetallicRoughness(3, GL30.GL_RGBA8, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE),
    Uv(4,GL30.GL_RGBA8, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE),
    Depth(5, GL30.GL_DEPTH_COMPONENT32F, GL30.GL_DEPTH_COMPONENT, GL30.GL_FLOAT);


    val attachment = GL30.GL_COLOR_ATTACHMENT0 + layout

    fun attach(builder: GLFrameBuffer.FrameBufferBuilder) {
            when (this) {
                Depth -> builder.addDepthTextureAttachment(internalFormat, type)
                else -> builder.addColorTextureAttachment(internalFormat, format, type)
            }
    }

    override fun toString() = name.lowercase()

    companion object {
        fun toFbo(vararg attachments: TextureAttachment, width: Int = Gdx.graphics.width,height: Int = Gdx.graphics.height): FrameBuffer {
            val builder = GLFrameBuffer.FrameBufferBuilder(width, height)
            attachments.forEach { attachment -> attachment.attach(builder) }
            return builder.build()
        }

        fun FrameBuffer.getTexture(attachment: TextureAttachment) = textureAttachments[attachment.layout]
    }


}
