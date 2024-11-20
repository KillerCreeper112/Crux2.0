package killercreepr.cruxblocks.block.active;

import com.destroystokyo.paper.ParticleBuilder;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.crux.api.block.sound.CreateBlockSoundGroup;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.item.component.ToolComponent;
import killercreepr.crux.core.util.CruxItem;
import killercreepr.cruxblocks.block.CruxBlock;
import killercreepr.cruxblocks.block.component.CruxBlockComponents;
import killercreepr.cruxblocks.block.context.BlockContext;
import killercreepr.cruxblocks.block.flag.BlockBreakFlag;
import killercreepr.cruxblocks.block.flag.BlockBreakFlags;
import killercreepr.cruxblocks.event.CruxBlockBreakEvent;
import killercreepr.cruxblocks.user.EntityMiner;
import killercreepr.cruxblocks.user.Miner;
import killercreepr.cruxblocks.user.Tooled;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;

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
        return breakBlock(miner, BlockBreakFlags.standard());
    }

    //todo add block flags inside of booleans for display effects and display drops
    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable Miner miner, @NotNull BlockBreakFlags flags){
        Block block = getBlock();
        Collection<ItemStack> drops;
        Entity entityMiner;
        if(miner instanceof EntityMiner m){
            entityMiner = m.getEntity();
        }else entityMiner = null;
        if(entityMiner instanceof HumanEntity p && p.getGameMode() == GameMode.CREATIVE){
            drops = null;
        }else if(canHarvest(miner)) drops = getDrops(miner);
        else drops = null;
        CruxBlockBreakEvent event = new CruxBlockBreakEvent(this, BlockContext.context(block, miner), drops);
        if(!event.callEvent()) return event;
        drops = event.getDrops();

        BlockData data = block.getBlockData();
        Crux.handlers().block().setType(block, Material.AIR);

        if(!flags.hasFlag(BlockBreakFlag.DISABLE_SOUND)){
            CruxBlock custom = getCruxBlock();
            CreateBlockSoundGroup soundGroup = custom.getComponents().getOrDefault(
                CruxBlockComponents.BLOCK_SOUND_GROUP, custom.getGroup().getComponents().get(CruxBlockComponents.BLOCK_SOUND_GROUP)
            );
            if(soundGroup != null){
                CreateSound sound = soundGroup.getBreakSound();
                if(sound != null){
                    sound.playAt(block.getLocation().toCenterLocation());
                }
            }
        }
        if(!flags.hasFlag(BlockBreakFlag.DISABLE_PARTICLES)){
            new ParticleBuilder(Particle.BLOCK)
                .count(10)
                .offset(.3, .3, .3)
                .extra(.1)
                .data(data)
                .location(block.getLocation().toCenterLocation().subtract(0, .5, 0))
                .spawn()
            ;
        }

        if(drops != null && !flags.hasFlag(BlockBreakFlag.DISABLE_DROPS)){
            Location x = block.getLocation().toCenterLocation().subtract(0, .5, 0);
            for(ItemStack i : drops){
                block.getWorld().dropItem(x, i);
            }
        }
        if(miner instanceof Tooled tooled){
            ItemStack tool = tooled.getTool();
            if(tool != null){
                if(entityMiner instanceof LivingEntity e){
                    if(!(e instanceof Player p) || p.getGameMode() != GameMode.CREATIVE){
                        e.damageItemStack(EquipmentSlot.HAND, 1);
                    }
                }else{
                    Crux.handlers().item().damageItem(tool, 1, entityMiner);
                }
            }
        }
        broken(event.getContext());
        return event;
    }

    default @NotNull CruxBlockBreakEvent breakBlock(@Nullable ItemStack tool){
        return breakBlock(Miner.item(tool), BlockBreakFlags.standard());
    }

    /**
     * Checks if this block is still valid based on the block that is representing it in the world.
     */
    boolean isValid();

    /**
     * Called to basically break the block if it's no longer in a valid location.
     * In the majority of implementations, the block should break if {@link #isPlacementValid()} returns false.
     * For example, a flower would probably want to break if it's no longer on a solid block.
     */
    default void update(){
        if(isPlacementValid()) return;
        breakBlock((Miner) null);
    }

    default boolean isPlacementValid(){
        return getCruxBlock().canPlace(BlockContext.context(getBlock(), null));
    }

    default boolean canHarvest(@Nullable Miner miner){
        boolean requiresCorrectToolForDrops = getCruxBlock().getComponents().getOrDefault(
            CruxBlockComponents.REQUIRES_CORRECT_TOOL_FOR_DROPS,
            getCruxBlock().getComponents().getOrDefault(CruxBlockComponents.REQUIRES_CORRECT_TOOL_FOR_DROPS, false)
        );
        if(!requiresCorrectToolForDrops) return true;

        Tooled tooled;
        if(miner instanceof Tooled m) tooled = m;
        else tooled = null;
        if(tooled == null || tooled.getTool() == null) return false;

        CruxItem cruxItem = CruxItem.create(tooled.getTool());
        ToolComponent toolComponent = cruxItem.get(CruxComponents.TOOL);
        if(toolComponent == null) return false;
        ToolComponent.Result result = toolComponent.test(Objects.requireNonNull(
            Crux.handlers().block().getBlock(getBlock())
        ));
        if(result == null) return false;
        return result.canHarvest();
    }

    @Nullable
    default Collection<ItemStack> getDrops(@Nullable Miner miner){
        return null;
    }

    default boolean isPreferredTool(@Nullable ItemStack item){
        return false;
    }

    default float getSpeedMultiplier(@Nullable Miner miner){
        Tooled tooled;
        if(miner instanceof Tooled m) tooled = m;
        else tooled = null;

        if(tooled == null) return 1f;
        ItemStack tool = tooled.getTool();
        if(tool == null) return 1f;

        CruxItem cruxItem = CruxItem.create(tool);
        ToolComponent toolComponent = cruxItem.get(CruxComponents.TOOL);
        if(toolComponent == null) return 1f;
        ToolComponent.Result result = toolComponent.test(Objects.requireNonNull(
            Crux.handlers().block().getBlock(getBlock())
        ));
        if(result == null) return 1f;
        return result.getSpeed();
    }

    @Nullable
    default Collection<ItemStack> getDrops(){ return getDrops(null); }

    default float getMineSpeed(@Nullable Miner miner, boolean includeEnchants){
        if(true){
            float speedMultiplier = getSpeedMultiplier(miner);

            float hardness = getCruxBlock().getComponents().getOrDefault(
                CruxComponents.HARDNESS, getCruxBlock().getGroup().getComponents().getOrDefault(CruxComponents.HARDNESS, 1f)
            );
            if(hardness <= 0D) return Float.MAX_VALUE;
            float speed = speedMultiplier / hardness;

            if(!canHarvest(miner)){
                speed /= 5;
            }

            return speed;
        }

        Tooled tooled;
        if(miner instanceof Tooled m) tooled = m;
        else tooled = null;
        //boolean preferredTool = isPreferredTool(tooled==null?null:tooled.getTool());
        boolean canHarvest = canHarvest(miner);

        float speedMultiplier = getSpeedMultiplier(miner);
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
                EntityEquipment equipment = lE.getEquipment();
                ItemStack helmet = equipment == null ? null : equipment.getHelmet();
                if(equipment == null || helmet == null || helmet.getEnchantmentLevel(Enchantment.AQUA_AFFINITY) < 1){
                    speedMultiplier /= 5f;
                }
            }
        }
        if(entityMiner != null && entityMiner.getEntity().getFallDistance() > 0f) speedMultiplier /= 5f;
        float damage = speedMultiplier / getCruxBlock().getComponents().getOrDefault(CruxComponents.HARDNESS, 1f);
        if(canHarvest) damage /= 30f;
        else damage /= 100f;
        return damage;
    }
}
