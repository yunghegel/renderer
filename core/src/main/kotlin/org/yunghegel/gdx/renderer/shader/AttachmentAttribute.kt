package org.yunghegel.gdx.renderer.shader

import com.badlogic.gdx.graphics.g3d.Attribute
import org.yunghegel.gdx.renderer.AttachmentSpec
import org.yunghegel.gdx.renderer.deffered.GBuffer
import org.yunghegel.gdx.renderer.deffered.TextureAttachment

class AttachmentAttribute (type:Long, val attachment: AttachmentSpec) : Attribute(type) {


    override fun compareTo(other: Attribute?): Int {
        if (type != other?.type) {
            return type.compareTo(other?.type ?: 0)
        } else {
            return if (attachment.type != (other as AttachmentAttribute).attachment.type) {
                attachment.type.compareTo((other as AttachmentAttribute).attachment.type)
            } else {
                0
            }
        }
    }



    override fun copy(): Attribute {
        return AttachmentAttribute(type, attachment)
    }


    companion object {
        @JvmStatic
        val PositionAlias = "g_position"
        @JvmStatic
        val Position = register(PositionAlias)
        @JvmStatic
        val NormalAlias = "g_normal"
        @JvmStatic
        val Normal = register(NormalAlias)
        @JvmStatic
        val SpecularAlias = "g_specular"
        @JvmStatic
        val Specular = register(SpecularAlias)
        @JvmStatic
        val UVAlias = "g_uv"
        @JvmStatic
        val UV = register(UVAlias)
        @JvmStatic
        val DepthAlias = "g_depth"
        @JvmStatic
        val Depth = register(DepthAlias)

        @JvmStatic fun create(attachment: TextureAttachment, gBuffer: GBuffer) : AttachmentAttribute {
            val texture = gBuffer.getTexture(attachment) ?: throw IllegalArgumentException("Attachment $attachment not found in GBuffer")
            return when(attachment) {
                TextureAttachment.Position -> AttachmentAttribute(Position, AttachmentSpec(attachment, texture))
                TextureAttachment.Normal -> AttachmentAttribute(Normal, AttachmentSpec(attachment,texture))
                TextureAttachment.Color -> AttachmentAttribute(Specular, AttachmentSpec(attachment,texture))
                TextureAttachment.MetallicRoughness -> AttachmentAttribute(Specular, AttachmentSpec(attachment, texture))
                TextureAttachment.Uv -> AttachmentAttribute(UV, AttachmentSpec(attachment, texture))
                TextureAttachment.Depth -> AttachmentAttribute(Depth, AttachmentSpec(attachment, texture))
            }
        }
    }

}
