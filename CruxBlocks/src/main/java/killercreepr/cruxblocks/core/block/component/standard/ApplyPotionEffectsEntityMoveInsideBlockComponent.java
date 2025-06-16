package killercreepr.cruxblocks.core.block.component.standard;

import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.cruxblocks.api.block.component.CruxEntityMoveInsideBlockComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ApplyPotionEffectsEntityMoveInsideBlockComponent implements CruxEntityMoveInsideBlockComponent {
    protected final Collection<PotionEffect> potionEffects;
    protected final EntityPredicate filter;

    public ApplyPotionEffectsEntityMoveInsideBlockComponent(Collection<PotionEffect> potionEffects, EntityPredicate filter) {
        this.potionEffects = potionEffects;
        this.filter = filter;
    }
    public Collection<PotionEffect> getPotionEffects() {
        return potionEffects;
    }

    public EntityPredicate getFilter() {
        return filter;
    }

    @Override
    public void onEntityMoveInside(@NotNull Entity e) {
        if(!(e instanceof LivingEntity lv)) return;
        if(filter != null && !filter.test(e)) return;
        lv.addPotionEffects(potionEffects);
    }
}
