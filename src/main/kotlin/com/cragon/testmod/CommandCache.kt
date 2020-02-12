package com.cragon.testmod

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import java.util.*

@Environment(EnvType.CLIENT)
class CommandCache {
    companion object {
        private val DISPATCHER = CommandDispatcher<ClientCommandSource>()

        @Throws(CommandSyntaxException::class)
        fun execute(input: String, source: ClientCommandSource): Int {
            return DISPATCHER.execute(input, source)
        }

        fun hasCommand(name: String): Boolean {
            return DISPATCHER.findNode(Collections.singleton(name)) != null
        }
    }
}