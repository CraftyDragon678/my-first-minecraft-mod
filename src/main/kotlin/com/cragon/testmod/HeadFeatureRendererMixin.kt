package com.cragon.testmod

import com.mojang.authlib.GameProfile
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.AbstractSkullBlock
import net.minecraft.block.entity.SkullBlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer
import net.minecraft.client.render.entity.feature.FeatureRenderer
import net.minecraft.client.render.entity.feature.FeatureRendererContext
import net.minecraft.client.render.entity.feature.HeadFeatureRenderer
import net.minecraft.client.render.entity.model.EntityModel
import net.minecraft.client.render.entity.model.ModelWithHead
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.ZombieVillagerEntity
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.item.ArmorItem
import net.minecraft.item.BlockItem
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtHelper
import net.minecraft.util.math.Direction
import org.apache.commons.lang3.StringUtils
import java.util.*

@Environment(EnvType.CLIENT)
class HeadFeatureRendererMixin<T : LivingEntity?, M>(context: FeatureRendererContext<T, M>?) :
    FeatureRenderer<T, M>(context) where M : EntityModel<T>?, M : ModelWithHead? {
    private val defaultRenderer = HeadFeatureRenderer(context)

    override fun render(
        matrixStack: MatrixStack,
        vertexConsumerProvider: VertexConsumerProvider,
        i: Int,
        livingEntity: T,
        f: Float,
        g: Float,
        h: Float,
        j: Float,
        k: Float,
        l: Float
    ) {
        if (!livingEntity!!.isInvisible) {
            defaultRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, f, g, h, j, k, l)
            return
        }
        val itemStack = livingEntity.getEquippedStack(EquipmentSlot.HEAD)
        if (!itemStack.isEmpty) {
            val item = itemStack.item
            matrixStack.push()
            val bl = livingEntity is VillagerEntity || livingEntity is ZombieVillagerEntity
            var p: Float
            if (livingEntity.isBaby && livingEntity !is VillagerEntity) {
                p = 2.0f
                val n = 1.4f
                matrixStack.translate(0.0, 0.03125, 0.0)
                matrixStack.scale(0.7f, 0.7f, 0.7f)
                matrixStack.translate(0.0, 1.0, 0.0)
            }
            (this.contextModel as ModelWithHead).head.rotate(matrixStack)
            if (item is BlockItem && item.block is AbstractSkullBlock) {
                p = 1.1875f
                matrixStack.scale(2f, -2f, -2f)
                matrixStack.translate(0.0, - 0.0625 * 3.5, 0.0)
                if (bl) {
                    matrixStack.translate(0.0, 0.0625, 0.0)
                }
                var gameProfile: GameProfile? = null
                if (itemStack.hasTag()) {
                    val compoundTag = itemStack.tag
                    if (compoundTag!!.contains("SkullOwner", 10)) {
                        gameProfile = NbtHelper.toGameProfile(compoundTag.getCompound("SkullOwner"))
                    } else if (compoundTag.contains("SkullOwner", 8)) {
                        val string = compoundTag.getString("SkullOwner")
                        if (!StringUtils.isBlank(string)) {
                            gameProfile = SkullBlockEntity.loadProperties(GameProfile(null as UUID?, string))
                            compoundTag.put("SkullOwner", NbtHelper.fromGameProfile(CompoundTag(), gameProfile))
                        }
                    }
                }
                matrixStack.translate(-0.5, 0.0, -0.5)
                SkullBlockEntityRenderer.render(
                    null as Direction?,
                    180.0f,
                    (item.block as AbstractSkullBlock).skullType,
                    gameProfile,
                    f,
                    matrixStack,
                    vertexConsumerProvider,
                    i
                )
            } else if (item !is ArmorItem || item.slotType != EquipmentSlot.HEAD) {
                p = 0.625f
                matrixStack.translate(0.0, -0.25, 0.0)
                matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f))
                matrixStack.scale(0.625f, -0.625f, -0.625f)
                if (bl) {
                    matrixStack.translate(0.0, 0.1875, 0.0)
                }
                MinecraftClient.getInstance().heldItemRenderer.renderItem(
                    livingEntity,
                    itemStack,
                    ModelTransformation.Mode.HEAD,
                    false,
                    matrixStack,
                    vertexConsumerProvider,
                    i
                )
            }
            matrixStack.pop()
        }
    }
}
