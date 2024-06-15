package dev.foxgirl.mace3d.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.foxgirl.mace3d.Mace3D;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {

    @Shadow @Final
    private ItemModelShaper itemModelShaper;

    @Unique
    private void mace3d$tryReplaceMaceBakedModelWith(
        ItemStack stack, LocalRef<BakedModel> modelRef,
        ModelResourceLocation modelIdentifier
    ) {
        if (stack.is(Items.MACE)) {
            modelRef.set(itemModelShaper.getModelManager().getModel(modelIdentifier));
        }
    }

    @Inject(
        method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V",
        at = @At(
            value = "INVOKE", ordinal = 0, shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private void mace3d$injected$renderItem(
        CallbackInfo info,
        @Local(ordinal = 0, argsOnly = true) ItemStack stack,
        @Local(ordinal = 0, argsOnly = true) LocalRef<BakedModel> modelRef
    ) {
        mace3d$tryReplaceMaceBakedModelWith(stack, modelRef, Mace3D.MACE);
    }

    @Inject(
        method = "getModel(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/client/resources/model/BakedModel;",
        at = @At(
            value = "INVOKE", ordinal = 0, shift = At.Shift.BY, by = 2,
            target = "Lnet/minecraft/client/renderer/ItemModelShaper;getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;"
        )
    )
    private void mace3d$injected$getModel(
        CallbackInfoReturnable<BakedModel> info,
        @Local(ordinal = 0, argsOnly = true) ItemStack stack,
        @Local(ordinal = 0, argsOnly = false) LocalRef<BakedModel> modelRef
    ) {
        mace3d$tryReplaceMaceBakedModelWith(stack, modelRef, Mace3D.MACE_IN_HAND);
    }

}
