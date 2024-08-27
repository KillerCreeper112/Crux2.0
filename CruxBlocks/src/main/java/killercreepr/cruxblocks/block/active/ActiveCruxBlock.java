package killercreepr.cruxblocks.block.active;

import com.destroystokyo.paper.ParticleBuilder;
import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.user.EntityMiner;
import killercreepr.cruxblocks.user.ItemMiner;
import killercreepr.cruxblocks.user.Miner;
import killercreepr.cruxblocks.user.Tooled;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a crux block that is placed into the world.
 */
public interface ActiveCruxBlock {
    default void placed(@NotNull BlockContext ctx){}
    default void broken(@NotNull BlockContext ctx){}
    @NotNull Block getBlock();
    @NotNull
    CruxBlock getCruxBlock();

    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable Miner miner){
        return breakBlock(miner, true, false);
    }

    //todo add block flags inside of booleans for display effects and display drops
    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable Miner miner, boolean displayEffects, boolean disableDrops){
        Block block = getBlock();
        Collection<ItemStack> drops;
        if(miner instanceof EntityMiner m && m.getEntity() instanceof Player p && p.getGameMode() == GameMode.CREATIVE){
            drops = null;
        }else drops = getDrops(miner);
        CruxBlockBreakEvent event = new CruxBlockBreakEvent(this, BlockContext.context(block, miner), drops);
        if(!event.callEvent()) return event;
        drops = event.getDrops();
        BlockData data = block.getBlockData();
        Crux.handlers().block().setType(getBlock(), Material.AIR);

        if(displayEffects){
            CruxBlock custom = getCruxBlock();
            CreateBlockSoundGroup soundGroup = custom.getSoundGroup();
            if(soundGroup != null){
                CreateSound sound = soundGroup.getBreakSound();
                if(sound != null){
                    sound.playAt(block.getLocation().toCenterLocation());
                }
            }

            new ParticleBuilder(Particle.BLOCK)
                .count(20)
                .offset(.5, .5, .5)
                .extra(.1)
                .data(data)
                .location(block.getLocation().toCenterLocation().subtract(0, .5, 0))
                .spawn()
            ;
        }

        if(drops != null){
            Location x = block.getLocation().toCenterLocation().subtract(0, .5, 0);
            for(ItemStack i : drops){
                block.getWorld().dropItem(x, i);
            }
        }
        /*todo if(e != null) GrimItem.damage(e, tool, 1, EquipmentSlot.HAND);
        else GrimItem.damage(tool, 1);*/
        broken(event.getContext());
        return event;
    }

    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable ItemStack tool){
        return breakBlock(new ItemMiner(tool), true, false);
    }

    /**
     * Checks if this block is still valid based on the block that is representing it in the world.
     */
    boolean isValid();

    /**
     * Called to basically break the block if it's no longer in a valid location.
     * For example, a flower would probably want to break if it's no longer on a solid block.
     */
    default void update(){
        if(getCruxBlock().canPlace(BlockContext.context(getBlock(), null))) return;
        breakBlock((Miner) null);
    }

    default boolean canHarvest(@Nullable Miner miner){
        return true;
    }

    @Nullable
    default Collection<ItemStack> getDrops(@Nullable Miner miner){
        return null;
    }

    default boolean isPreferredTool(@Nullable ItemStack item){ return false; }

    @Nullable
    default Collection<ItemStack> getDrops(){ return getDrops(null); }

    default float getMineSpeed(@Nullable Miner miner, boolean includeEnchants){
        Tooled tooled;
        if(miner instanceof Tooled m) tooled = m;
        else tooled = null;
        boolean preferredTool = isPreferredTool(tooled==null?null:tooled.getTool());
        boolean canHarvest = canHarvest(miner);

        float speedMultiplier = 1f;
        if(canHarvest && includeEnchants) {
            //enchants
        }
        //if(preferredTool) speedMultiplier = (float) GrimAttribute.get(e, GrimAttribute.MINING_SPEED);

        EntityMiner entityMiner;
        if(miner instanceof EntityMiner m) entityMiner = m;
        else entityMiner = null;

        if(entityMiner != null && entityMiner.getEntity() instanceof LivingEntity lE){
            if(lE.hasPotionEffect(PotionEffectType.HASTE)){
                speedMultiplier *= 0.2f * (lE.getPotionEffect(PotionEffectType.HASTE).getAmplifier() + 1f);
            }
            if(lE.hasPotionEffect(PotionEffectType.MINING_FATIGUE)){
                speedMultiplier *= Math.pow(.3f, Math.min(lE.getPotionEffect(PotionEffectType.MINING_FATIGUE).getAmplifier(), 4));
            }
            if(lE.getEyeLocation().getBlock().getType() == Material.WATER /*&& doesNotHaveAquaAffinity*/){
                speedMultiplier /= 5f;
            }
        }
        if(entityMiner != null && entityMiner.getEntity().getFallDistance() > 0f) speedMultiplier /= 5f;
        float damage = speedMultiplier / getCruxBlock().getHardness();
        if(canHarvest) damage /= 30f;
        else damage /= 100f;
        return damage;
    }
}
