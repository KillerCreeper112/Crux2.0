package killercreepr.cruxblocks.block;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.event.CruxBlockPlaceEvent;
import net.kyori.adventure.key.Keyed;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface CruxBlock extends Keyed, CruxBlockData {
    @NotNull ActiveCruxBlock createActive(@NotNull Block block);
    @Override
    default float getHardness(){
        CruxBlockGroup group = getGroup();
        Objects.requireNonNull(group, "CruxBlock getHardness method has not been overridden and does not have a group set!");
        return group.getHardness();
    }
    @NotNull TextureData getTextureData();
    @Nullable CruxBlockGroup getGroup();
    void setGroup(@Nullable CruxBlockGroup group);

    @Override
    default boolean canPlace(@NotNull BlockContext ctx){
        CruxBlockGroup group = getGroup();
        Objects.requireNonNull(group, "CruxBlock canPlace method has not been overridden and does not have a group set!");
        return group.canPlace(ctx);
    }

    /**
     * Skips the can place check and event call.
     */
    default void setBlock(@NotNull BlockContext ctx, boolean applyPhysics){
        TextureData data = getTextureData();
        data.applyToBlock(ctx.getBlock(), applyPhysics);
    }

    @Override
    @Nullable
    default SoundGroup getSoundGroup(){
        CruxBlockGroup group = getGroup();
        Objects.requireNonNull(group);
        return group.getSoundGroup();
    }


    @Override
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics){
        if(!canPlace(ctx)) return null;
        CruxBlockPlaceEvent event = new CruxBlockPlaceEvent(this, ctx);
        if(!event.callEvent()) return null;
        Block b = ctx.getBlock();

        TextureData data = getTextureData();
        data.setBlock(b, applyPhysics);
        ActiveCruxBlock active = CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class)
            .getActiveBlock(ctx.getBlock());
        if(active != null){
            if(getSoundGroup() != null){
                b.getWorld().playSound(b.getLocation().toCenterLocation(), getSoundGroup().getPlaceSound(), getSoundGroup().getVolume(), getSoundGroup().getPitch());
            }
            active.placed(ctx);
            //if(active instanceof ActiveTickable) DP.blocks().addTickedBlock(active);
        }
        return active;
    }
}
