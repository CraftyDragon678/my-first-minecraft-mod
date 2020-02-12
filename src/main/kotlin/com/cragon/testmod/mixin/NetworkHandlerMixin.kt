package com.cragon.testmod.mixin

import com.cragon.testmod.CommandCache
import com.mojang.authlib.GameProfile
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket
import net.minecraft.server.command.CommandSource
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ClientPlayNetworkHandler::class)
class NetworkHandlerMixin {
    @Shadow
    private lateinit var commandDispatcher: CommandDispatcher<CommandSource>


    @Inject(method = ["onCommandTree"], at = [At("RETURN")])
    private fun onCommandTree(packet: CommandTreeS2CPacket, ci: CallbackInfo) {
        addCommands()
    }

    @Inject(method = ["<init>"], at = [At("RETURN")])
    private fun onConstruct(mc: MinecraftClient, screen: Screen, cc: ClientConnection, gp: GameProfile, ci: CallbackInfo) {
        addCommands()
//        CommandCache.build()
    }

    @Unique
    @SuppressWarnings("unchecked")
    private fun addCommands() {

    }
}