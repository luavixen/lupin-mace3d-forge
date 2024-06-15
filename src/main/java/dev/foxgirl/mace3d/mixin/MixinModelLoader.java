package dev.foxgirl.mace3d.mixin;

import dev.foxgirl.mace3d.Mace3D;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBakery.class)
public abstract class MixinModelLoader {

    @Shadow
    private void loadSpecialItemModelAndDependencies(ModelResourceLocation id) {
        throw new AssertionError();
    }

    @Inject(
        method = "<init>(Lnet/minecraft/client/color/block/BlockColors;Lnet/minecraft/util/profiling/ProfilerFiller;Ljava/util/Map;Ljava/util/Map;)V",
        at = @At("TAIL")
    )
    private void mace3d$injected$__init__(CallbackInfo info) {
        loadSpecialItemModelAndDependencies(Mace3D.MACE_IN_HAND);
    }

}
