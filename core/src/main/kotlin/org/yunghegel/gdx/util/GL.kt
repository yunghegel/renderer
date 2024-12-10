package org.yunghegel.gdx.util

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion


fun Texture.flip(x:Boolean, y: Boolean) : Texture {
    val tex = TextureRegion(this).apply { flip (x,y) }
    this.dispose()
    return tex.texture
}
