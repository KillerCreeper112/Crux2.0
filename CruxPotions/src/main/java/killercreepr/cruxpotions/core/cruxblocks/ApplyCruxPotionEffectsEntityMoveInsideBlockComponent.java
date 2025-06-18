package killercreepr.cruxpotions.core.cruxblocks;

import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.cruxblocks.api.block.component.CruxEntityMoveInsideBlockComponent;
import killercreepr.cruxpotions.api.entity.PotionHolder;
import killercreepr.cruxpotions.api.potion.StoredPotion;
import killercreepr.cruxpotions.api.potion.inflictor.PotionInflictor;
import killercreepr.cruxpotions.core.entity.memory.SimplePotionHolder;
import killercreepr.cruxpotions.core.potions.inflictor.BlockInflictor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ApplyCruxPotionEffectsEntityMoveInsideBlockComponent implements CruxEntityMoveInsideBlockComponent {
    protected final Collection<StoredPotion> potionEffects;
    protected final EntityPredicate filter;

    public ApplyCruxPotionEffectsEntityMoveInsideBlockComponent(Collection<StoredPotion> potionEffects, EntityPredicate filter) {
        this.potionEffects = potionEffects;
        this.filter = filter;
    }
    public Collection<StoredPotion> getPotionEffects() {
        return potionEffects;
    }

    public EntityPredicate getFilter() {
        return filter;
    }

    @Override
    public void onEntityMoveInside(@NotNull Entity e) {
        if(!(e instanceof LivingEntity lv)) return;
        if(filter != null && !filter.test(e)) return;
        PotionHolder potionHolder = EntityMemory.getOrCreateDataHolder(e, SimplePotionHolder.class);
        if(potionHolder == null) return;
        PotionInflictor inflictor = new BlockInflictor(e.getLocation().getBlock());

        for (StoredPotion effect : potionEffects) {
            potionHolder.addPotion(effect.create(e, inflictor));
        }
    }
}
