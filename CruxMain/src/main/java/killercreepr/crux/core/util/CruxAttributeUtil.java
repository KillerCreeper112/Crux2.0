package killercreepr.crux.core.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.function.Consumer;

public class CruxAttributeUtil {
    public static boolean editInstance(LivingEntity e, Attribute attribute, Consumer<AttributeInstance> consumer){
        if(e == null) return false;
        var instance = e.getAttribute(attribute);
        if(instance == null) return false;
        consumer.accept(instance);
        return true;
    }

    public static boolean editInstance(Entity e, Attribute attribute, Consumer<AttributeInstance> consumer){
        if(!(e instanceof LivingEntity le)) return false;
        return editInstance(le, attribute, consumer);
    }
}
