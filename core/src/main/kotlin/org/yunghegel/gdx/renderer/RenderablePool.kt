package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.utils.FlushablePool
import com.badlogic.gdx.utils.Pools


object RenderablePool : FlushablePool<Renderable>() {

    init {
        Pools.set(Renderable::class.java, this)
    }

    override fun newObject(): Renderable {
        return Renderable()
    }

    override fun obtain(): Renderable {
        val renderable = super.obtain()
        renderable.environment = null
        renderable.material = null
        renderable.meshPart["", null, 0, 0] = 0
        renderable.shader = null
        renderable.userData = null
        return renderable
    }
}