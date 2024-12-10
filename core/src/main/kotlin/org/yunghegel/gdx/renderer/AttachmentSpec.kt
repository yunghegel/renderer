package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.Texture
import org.yunghegel.gdx.renderer.deffered.TextureAttachment

data class AttachmentSpec(val type : TextureAttachment, val name : String, val texture : Texture) {
    constructor(type: TextureAttachment, texture: Texture) : this(type, type.name, texture)
}
