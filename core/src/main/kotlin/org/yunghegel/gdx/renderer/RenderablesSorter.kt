package org.yunghegel.gdx.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.utils.DefaultRenderableSorter
import com.badlogic.gdx.utils.Array

class RenderablesSorter : DefaultRenderableSorter() {

    override fun sort(camera: Camera?, renderables: Array<Renderable>?) {
        super.sort(camera, renderables)
    }

    override fun compare(o1: Renderable?, o2: Renderable?): Int {
        return super.compare(o1, o2)
    }
}