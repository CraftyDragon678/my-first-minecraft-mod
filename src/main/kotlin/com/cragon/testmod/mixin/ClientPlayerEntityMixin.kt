package com.cragon.testmod.mixin

import com.cragon.testmod.CommandCache
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientCommandSource
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.command.CommandException
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(ClientPlayerEntity::class)
abstract class ClientPlayerEntityMixin {
    @Shadow @Final protected lateinit var client: MinecraftClient
    @Shadow @Final lateinit var networkHandler: ClientPlayNetworkHandler
    @Shadow abstract fun addChatMessage(message: Text, bl: Boolean)

    @Inject(method = ["sendChatMessage"], at = [At("HEAD")], cancellable = true)
    private fun onChatMessage(msg: String, info: CallbackInfo) {
        if (msg.length < 2 || !msg.startsWith("/")) return
        if (!CommandCache.hasCommand(msg.substring(1).split(" ").toTypedArray()[0])) return
        var cancel = false
        try { // The game freezes when using heavy commands. Run your heavy code somewhere else pls
            val result: Int = CommandCache.execute(
                msg.substring(1), ClientCommandSource(networkHandler, client) as com.cragon.testmod.ClientCommandSource
            )
            if (result != 0) // Prevent sending the message
                cancel = true
        } catch (e: CommandException) {
            addChatMessage(e.textMessage.formatted(Formatting.RED), false)
            cancel = true
        } catch (e: CommandSyntaxException) {
            addChatMessage(LiteralText(e.message).formatted(Formatting.RED), false)
            cancel = true
        } catch (e: Exception) {
            addChatMessage(TranslatableText("command.failed").formatted(Formatting.RED), false)
            cancel = true
        }
        if (cancel) info.cancel()
    }
}