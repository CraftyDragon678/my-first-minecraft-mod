package com.cragon.testmod.mixin

import com.cragon.testmod.HeadFeatureRendererMixin
import net.minecraft.client.render.entity.ArmorStandEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel
import net.minecraft.client.render.entity.model.ArmorStandEntityModel
import net.minecraft.entity.decoration.ArmorStandEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Suppress("unused")
@Mixin(ArmorStandEntityRenderer::class)
abstract class ArmorStandEntityRendererMixin(entityRenderDispatcher : EntityRenderDispatcher) :
    LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>(entityRenderDispatcher,
        ArmorStandEntityModel() as ArmorStandArmorEntityModel?, 0F) {


    @Inject(method = ["<init>()V"], at = [At("TAIL")])
    fun init(ci: CallbackInfo) {
        features.removeAt(3)
        addFeature(HeadFeatureRendererMixin(this))
    }
}