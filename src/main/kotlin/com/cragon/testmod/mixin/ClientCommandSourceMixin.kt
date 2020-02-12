package com.cragon.testmod.mixin

import com.cragon.testmod.ClientCommandSource
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(net.minecraft.client.network.ClientCommandSource::class)
abstract class ClientCommandSourceMixin : ClientCommandSource {
    @Shadow
    @Final
    private lateinit var client: MinecraftClient

    override fun sendFeedback(text: Text) {
        client.player!!.addChatMessage(text, false)
    }

    override fun sendError(text: Text) {
        client.player!!.addChatMessage(LiteralText("").append(text).formatted(Formatting.RED), false)
    }
}