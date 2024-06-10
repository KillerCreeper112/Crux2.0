package killercreepr.cruxblocks.block;

import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.event.CruxBlockPlaceEvent;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface CruxBlock extends Keyed {
    @NotNull ActiveCruxBlock createActive(@NotNull Block block);
    float getHardness();
    @NotNull TextureData getTextureData();

    default @Nullable ActiveCruxBlock placeBlock(@NotNull BlockContext ctx){
        return placeBlock(ctx, true);
    }

    default boolean canPlace(@NotNull BlockContext ctx){
        return true;
    }

    /**
     * Skips the can place check and event call.
     */
    default void setBlock(@NotNull BlockContext ctx, boolean applyPhysics){
        TextureData data = getTextureData();
        data.applyToBlock(ctx.getBlock(), applyPhysics);
    }

    @Nullable
    default SoundGroup getSoundGroup(){
        return defaultSoundGroup();
    }

    default @Nullable ActiveCruxBlock placeBlock(@NotNull BlockContext ctx, boolean applyPhysics){
        if(!canPlace(ctx)) return null;
        CruxBlockPlaceEvent event = new CruxBlockPlaceEvent(this, ctx);
        if(!event.callEvent()) return null;

        TextureData data = getTextureData();
        data.applyToBlock(ctx.getBlock(), applyPhysics);
        //todo get active block and all that
        /*ActiveBlock active = getActiveBlock(b);
        if(active != null){
            if(getSoundGroup() != null){
                b.getWorld().playSound(b.getLocation(), getSoundGroup().getPlaceSound(), getSoundGroup().getVolume(), getSoundGroup().getPitch());
            }
            active.placed(e);
            //if(active instanceof ActiveTickable) DP.blocks().addTickedBlock(active);
        }*/
        return null;
    }

    static @NotNull SoundGroup defaultSoundGroup(){
        return new SoundGroup() {
            @Override
            public float getVolume() {
                return 1f;
            }

            @Override
            public float getPitch() {
                return 1f;
            }

            @Override
            public @NotNull Sound getBreakSound() {
                return Sound.BLOCK_STONE_BREAK;
            }

            @Override
            public @NotNull Sound getStepSound() {
                return Sound.BLOCK_STONE_STEP;
            }

            @Override
            public @NotNull Sound getPlaceSound() {
                return Sound.BLOCK_STONE_PLACE;
            }

            @Override
            public @NotNull Sound getHitSound() {
                return Sound.BLOCK_STONE_HIT;
            }

            @Override
            public @NotNull Sound getFallSound() {
                return Sound.BLOCK_STONE_FALL;
            }
        };
    }
}
