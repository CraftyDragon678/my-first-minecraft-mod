package com.cragon.testmod.mixin

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.ArmorStandEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.LivingEntityRenderer
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel
import net.minecraft.client.render.entity.model.ArmorStandEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin

@Suppress("unused")
@Mixin(ArmorStandEntityRenderer::class)
class ArmorStandEntityRendererMixin(entityRenderDispatcher: EntityRenderDispatcher?) :
    LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>(
        entityRenderDispatcher,
        ArmorStandEntityModel(),
        0F
    ) {
    val SKIN = Identifier("textures/entity/armorstand/wood.png")

    init {
        this.addFeature(HeadFeatureRenderer(this))
    }

    override fun render(
        livingEntity: ArmorStandEntity?,
        f: Float,
        g: Float,
        matrixStack: MatrixStack?,
        vertexConsumerProvider: VertexConsumerProvider?,
        i: Int
    ) {
        MinecraftClient.getInstance().player!!.sendMessage(Text.Serializer.fromJson("""{"text":"debug"}"""))
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i)
    }

    override fun getTexture(entity: ArmorStandEntity?): Identifier {
        return SKIN
    }
}