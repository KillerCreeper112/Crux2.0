package killercreepr.cruxtickables.core.entity.tickable;

import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

public class AttributesTickable extends SimpleActiveEntityTickable {
    protected final Map<Attribute, AttributeModifier> attributes;
    public AttributesTickable(Entity entity, EntityTickable tickable, CruxSlot slot, Map<Attribute, AttributeModifier> attributes) {
        super(entity, tickable, slot);
        this.attributes = attributes;
    }

    public boolean canApplyAttributes(Entity e){
        return true;
    }

    public void applyAttributes(LivingEntity li){
        attributes.forEach((att, mod) ->{
            var instance = li.getAttribute(att);
            if(instance == null) return;
            instance.removeModifier(mod.key());
            instance.addTransientModifier(mod);
        });
    }

    public void removeAttributes(LivingEntity li){
        attributes.forEach((att, mod) ->{
            var instance = li.getAttribute(att);
            if(instance == null) return;
            instance.removeModifier(mod.key());
        });
    }

    @Override
    public void started() {
        super.started();
        if(!canApplyAttributes(entity)) return;
        if(!(entity instanceof LivingEntity li)) return;
        applyAttributes(li);
    }

    @Override
    public void stopped() {
        super.stopped();
        if(!(entity instanceof LivingEntity li)) return;
        removeAttributes(li);
    }

    @Override
    public void tick() {

    }
}
