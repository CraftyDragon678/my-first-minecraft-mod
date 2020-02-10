package com.cragon.testmod.mixin

import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.Camera
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.Entity
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.BlockView
import net.minecraft.world.RayTraceContext
import net.minecraft.world.RayTraceContext.FluidHandling
import net.minecraft.world.RayTraceContext.ShapeType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import kotlin.math.max


@Suppress("unused")
@Mixin(Camera::class)
class CameraMixin {
    @Shadow
    private lateinit var area: BlockView
    @Shadow
    private lateinit var pos: Vec3d
    @Shadow
    private lateinit var focusedEntity: Entity
    @Shadow
    private lateinit var horizontalPlane: Vector3f


    @Inject(at = [At("HEAD")], method = ["clipToSpace"], cancellable = true)
    private fun onClipToSpace(
        desiredCameraDistance: Double,
        cir: CallbackInfoReturnable<Double>
    ) {
        val client = MinecraftClient.getInstance()

        if (client.gameRenderer.camera.isThirdPerson) {
            var dd = max(4.0, (client.options.fov - 70) / 3 + 3)
            for (i in 0..7) {
                var f = ((i and 1) * 2 - 1).toFloat()
                var g = ((i shr 1 and 1) * 2 - 1).toFloat()
                var h = ((i shr 2 and 1) * 2 - 1).toFloat()
                f *= 0.1f
                g *= 0.1f
                h *= 0.1f
                val vec3d = pos.add(f.toDouble(), g.toDouble(), h.toDouble())
                val vec3d2 = Vec3d(
                    pos.x - this.horizontalPlane.x.toDouble() * dd + f.toDouble() + h.toDouble(),
                    pos.y - this.horizontalPlane.y.toDouble() * dd + g.toDouble(),
                    pos.z - this.horizontalPlane.z.toDouble() * dd + h.toDouble()
                )
                val hitResult: HitResult = area.rayTrace(
                    RayTraceContext(
                        vec3d,
                        vec3d2,
                        ShapeType.COLLIDER,
                        FluidHandling.NONE,
                        this.focusedEntity
                    )
                )
                if (hitResult.type != HitResult.Type.MISS) {
                    val d = hitResult.pos.distanceTo(pos)
                    if (d < dd) {
                        dd = d
                    }
                }
            }

            cir.returnValue = dd
        }
    }
}