//@file:JvmName("Lwjgl3Launcher")

package org.yunghegel.gdx.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import org.yunghegel.gdx.WebTest

object Lwjgl3Launcher {
    @JvmStatic
    fun main(args: Array<String>) {
        Lwjgl3Application(WebTest(), Lwjgl3ApplicationConfiguration().apply {
            setTitle("gdx-web-test")
            setWindowedMode(1280, 720)
            setBackBufferConfig(8, 8, 8, 8, 32, 0, 4)
            setOpenGLEmulation(Lwjgl3ApplicationConfiguration.GLEmulation.GL32,3,3)
            setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
        })
    }
}

/** Launches the desktop (LWJGL3) application. */

