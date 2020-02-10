package com.cragon.testmod.mixin

import com.cragon.testmod.HeadFeatureRendererMixin
import net.minecraft.client.render.entity.ArmorStandEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import org.spongepowered.asm.mixin.Mixin

@Suppress("unused")
@Mixin(ArmorStandEntityRenderer::class)
class ArmorStandEntityRendererMixin(entityRenderDispatcher: EntityRenderDispatcher?) :
    ArmorStandEntityRenderer(entityRenderDispatcher) {

    init {
        this.features.removeAt(3)
        this.addFeature(HeadFeatureRendererMixin(this))
    }
}
