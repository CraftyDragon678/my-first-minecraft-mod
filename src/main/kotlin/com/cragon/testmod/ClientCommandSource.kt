package com.cragon.testmod

import net.minecraft.server.command.CommandSource
import net.minecraft.text.Text

interface ClientCommandSource : CommandSource {
    fun sendFeedback(text: Text)
    fun sendError(text: Text)
}