package org.yunghegel.gdx.teavm

import com.github.xpenatan.gdx.backends.teavm.config.AssetFileHandle
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuildConfiguration
import com.github.xpenatan.gdx.backends.teavm.config.TeaBuilder
import com.github.xpenatan.gdx.backends.teavm.config.plugins.TeaReflectionSupplier
import com.github.xpenatan.gdx.backends.teavm.gen.SkipClass
import org.teavm.vm.TeaVMOptimizationLevel
import java.io.File

/** Builds the TeaVM/HTML application. */
@SkipClass
object TeaVMBuilder {
    @JvmStatic fun main(arguments: Array<String>) {
        val teaBuildConfiguration = TeaBuildConfiguration().apply {
            assetsPath.add(AssetFileHandle("../assets"))
            webappPath = File("build/dist").canonicalPath
            // Register any extra classpath assets here:
            // additionalAssetsClasspathFiles += "org/yunghegel/gdx/asset.extension"
        }
        val reflectionPackage = "com.badlogic.gdx.math"
        TeaReflectionSupplier.addReflectionClass(reflectionPackage)
        // Register any classes or packages that require reflection here:
        // TeaReflectionSupplier.addReflectionClass("org.yunghegel.gdx.reflect")

        val tool = TeaBuilder.config(teaBuildConfiguration)
        tool.setOptimizationLevel(TeaVMOptimizationLevel.FULL);
        tool.mainClass = "org.yunghegel.gdx.teavm.TeaVMLauncher"
        TeaBuilder.build(tool)
    }
}
