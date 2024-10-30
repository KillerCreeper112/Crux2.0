package killercreepr.cruxblocks.block;

import killercreepr.crux.component.DataComponentHandler;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.block.active.ActiveCruxBlock;
import killercreepr.cruxblocks.block.active.ActiveCruxBlockImpl;
import killercreepr.cruxblocks.block.component.CruxBlockComponent;
import killercreepr.cruxblocks.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.context.PlaceBlockContext;
import killercreepr.cruxblocks.block.data.CustomBlockData;
import killercreepr.cruxblocks.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.block.texture.TextureData;
import killercreepr.cruxblocks.event.CruxBlockPlaceEvent;
import killercreepr.cruxblocks.event.CruxBlockSetEvent;
import killercreepr.cruxblocks.persistence.CruxBlocksPersistTags;
import killercreepr.cruxblocks.registries.CruxBlocksRegistries;
import net.kyori.adventure.key.Keyed;
import org.bukkit.block.Block;
import org.bukkit.generator.LimitedRegion;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface CruxBlock extends Keyed, CruxBlockData {
    default @NotNull ActiveCruxBlock createActive(@NotNull Block block){
        for(CruxBlockComponent c : getComponents().getAllOfType(CruxBlockComponent.class)){
            ActiveCruxBlock x = c.createActive(block, this);
            if(x != null) return x;
        }
        return new ActiveCruxBlockImpl(block, this);
    }
    @NotNull TextureData getTextureData();
    @Nullable CruxBlockGroup getGroup();
    void setGroup(@Nullable CruxBlockGroup group);

    @Override
    default boolean canPlace(@NotNull BlockContext ctx){
        for(CruxBlockComponent c : getComponents().getAllOfType(CruxBlockComponent.class)){
            Boolean x = c.canPlace(ctx, this);
            if(x != null) return x;
        }
        CruxBlockGroup group = getGroup();
        Objects.requireNonNull(group, "CruxBlock canPlace method has not been overridden and does not have a group set!");
        return group.canPlace(ctx);
    }

    /**
     * Skips the can place check and event call.
     */
    default @Nullable ActiveCruxBlock setBlock(@NotNull BlockContext ctx, boolean applyPhysics){
        CruxBlockSetEvent event = new CruxBlockSetEvent(this, ctx);
        if(!event.callEvent()) return null;

        TextureData data = getTextureData();
        data.setBlock(ctx.getBlock(), applyPhysics);
        if(CruxBlocksRegistries.BLOCK.getPossibleBlocks(data).size() > 1){
            CustomBlockData blockData = CustomBlockData.wrap(ctx.getBlock());
            CruxBlocksPersistTags.CRUX_BLOCK_KEY.set(blockData, key());
        }
        return CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class)
            .getActiveBlock(ctx.getBlock());
    }

    @Override
    default @Nullable ActiveCruxBlock placeBlock(@NotNull PlaceBlockContext ctx, boolean applyPhysics){
        if(!canPlace(ctx)) return null;
        CruxBlockPlaceEvent event = new CruxBlockPlaceEvent(this, ctx);
        if(!event.callEvent()) return null;
        Block b = ctx.getBlock();

        TextureData data = getTextureData();
        //todo add bukkit place block
        /*if(ctx.getMiner() instanceof EntityMiner m && m.getEntity() instanceof Player p){
            BlockState replacedState = b.getState(true);
            data.setBlock(b, false);
            BlockPlaceEvent bukkitPlace = new BlockPlaceEvent(b, replacedState, b.getRelative(ctx.getBlockFace()),
                m.getTool() == null ? p.getInventory().getItemInMainHand() : m.getTool(), p, true,
                m.getHand() == null ? EquipmentSlot.HAND : m.getHand()
            );
            if(!bukkitPlace.callEvent()){
                Crux.handlers().block().setType(b, Material.AIR, false, true);
                return null;
            }
        }else{
            data.setBlock(b, false);
        }*/

        data.setBlock(b, applyPhysics, true, block ->{
            if(CruxBlocksRegistries.BLOCK.getPossibleBlocks(data).size() > 1){
                CustomBlockData blockData = CustomBlockData.wrap(block);
                CruxBlocksPersistTags.CRUX_BLOCK_KEY.set(blockData, key());
            }
        });
        ActiveCruxBlock active = CruxRegistries.MODULES.getModuleOrThrow(CruxBlocksModule.class)
            .getActiveBlock(ctx.getBlock());
        if(active != null){
            CruxBlockGroup group = getGroup();
            CreateBlockSoundGroup soundGroup = getComponents().getOrDefault(
                CruxBlockComponents.BLOCK_SOUND_GROUP, group == null ? null : group.getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
            );
            if(soundGroup != null){
                CreateSound sound = soundGroup.getPlaceSound();
                if(sound != null){
                    sound.playAt(b.getLocation().toCenterLocation());
                }
            }
            active.placed(ctx);
            //if(active instanceof ActiveTickable) DP.blocks().addTickedBlock(active);
        }
        return active;
    }

    @ApiStatus.Experimental
    default void setBlock(@NotNull LimitedRegion region, int x, int y, int z){
        TextureData data = getTextureData();
        data.setBlock(region, x, y, z);
    }

    @Override
    default @NotNull DataComponentHandler getComponents(){
        CruxBlockGroup group = getGroup();
        if(group == null) throw new UnsupportedOperationException(this + " must override getComponents() because group is not set!");
        return group.getComponents();
    }
}
