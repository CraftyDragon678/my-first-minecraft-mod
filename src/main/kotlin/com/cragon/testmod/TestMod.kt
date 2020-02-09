package com.cragon.testmod

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry
import net.minecraft.client.util.InputUtil
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.lwjgl.glfw.GLFW

val zoomInKey: FabricKeyBinding = FabricKeyBinding.Builder.create(
    Identifier("testmod", "zoom_in"),
    InputUtil.Type.KEYSYM,
    GLFW.GLFW_KEY_EQUAL,
    "test mod"
).build()

val zoomOutKey: FabricKeyBinding = FabricKeyBinding.Builder.create(
    Identifier("testmod", "zoom_out"),
    InputUtil.Type.KEYSYM,
    GLFW.GLFW_KEY_MINUS,
    "test mod"
).build()


@Suppress("unused")
fun init() {
    val item = Item(Item.Settings().group(ItemGroup.MISC))

    Registry.register(Registry.ITEM, Identifier("testmod", "test_item"), item)


    KeyBindingRegistry.INSTANCE.addCategory("test mod")
    KeyBindingRegistry.INSTANCE.register(zoomInKey)
    KeyBindingRegistry.INSTANCE.register(zoomOutKey)
}



