package com.cragon.testmod.mixin

import net.minecraft.client.render.entity.ArmorStandEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel
import net.minecraft.client.render.entity.model.ArmorStandEntityModel
import net.minecraft.entity.decoration.ArmorStandEntity
import org.spongepowered.asm.mixin.Mixin

@Suppress("unused")
@Mixin(ArmorStandEntityRenderer::class)
abstract class ArmorStandEntityRendererMixin(
    entityRenderDispatcher: EntityRenderDispatcher?
) : LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>(entityRenderDispatcher,
    ArmorStandEntityModel(), 0f) {

    


}