package com.cragon.testmod.mixin

import com.cragon.testmod.zoomInKey
import com.cragon.testmod.zoomOutKey
import net.minecraft.client.MinecraftClient
import net.minecraft.client.options.GameOptions
import net.minecraft.client.render.GameRenderer
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SynchronousResourceReloadListener
import org.objectweb.asm.Opcodes
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import kotlin.math.max
import kotlin.math.min


@Suppress("unused")
@Mixin(GameRenderer::class)
class GameRendererMixin : AutoCloseable, SynchronousResourceReloadListener {

    @Redirect(
        at = At(
            value = "FIELD",
            target = "Lnet/minecraft/client/options/GameOptions;fov:D",
            opcode = Opcodes.GETFIELD,
            ordinal = 0
        ), method = ["getFov(Lnet/minecraft/client/render/Camera;FZ)D"]
    )
    private fun getFov(options: GameOptions): Double {
        val options = MinecraftClient.getInstance().options
        if (zoomInKey.isPressed && zoomOutKey.isPressed) {
            options.fov = 70.0
        } else if (zoomInKey.isPressed) {
            options.fov -= 1
        } else if (zoomOutKey.isPressed) {
            options.fov += 1
        }
        options.fov = min(max(1.0, options.fov), 155.0)
        return options.fov
    }

    override fun apply(manager: ResourceManager?) {
    }

    override fun close() {
    }
}