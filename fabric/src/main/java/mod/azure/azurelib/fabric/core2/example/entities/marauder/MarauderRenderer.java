package mod.azure.azurelib.fabric.core2.example.entities.marauder;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.core2.render.entity.AzEntityRenderer;
import mod.azure.azurelib.core2.render.entity.AzEntityRendererConfig;
import mod.azure.azurelib.core2.render.layer.AzAutoGlowingLayer;
import org.jetbrains.annotations.NotNull;

public class MarauderRenderer extends AzEntityRenderer<MarauderEntity> {

    private static final ResourceLocation MODEL = AzureLib.modResource("geo/entity/marauder.geo.json");

    private static final ResourceLocation TEXTURE = AzureLib.modResource("textures/entity/marauder.png");

    public MarauderRenderer(EntityRendererProvider.Context context) {
        super(
            AzEntityRendererConfig.<MarauderEntity>builder(MODEL, TEXTURE)
                .addRenderLayer(new AzAutoGlowingLayer<>())
                .setAnimatorProvider(MarauderAnimator::new)
                .setDeathMaxRotation(0F)
                .build(),
            context
        );
    }

    @Override
    public void render(@NotNull MarauderEntity entity, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        var isMovingOnGround = entity.moveAnalysis.isMovingHorizontally() && entity.onGround();

        if (!entity.isAlive()) {
            entity.animationDispatcher.death();
        } else if (isMovingOnGround) {
            if (entity.isAggressive()) {
                entity.animationDispatcher.run();
            } else {
                entity.animationDispatcher.walk();
            }
        } else {
            entity.animationDispatcher.idle();
        }
    }
}
