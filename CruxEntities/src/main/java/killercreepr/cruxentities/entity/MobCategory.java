package killercreepr.cruxentities.entity;

import killercreepr.crux.Crux;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Keyed;

public interface MobCategory extends Keyed {
    static void register(){}
    MobCategory NEUTRAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("neutral")));
    MobCategory ANIMAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("animal")));
    MobCategory MONSTER = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("monster")));
    MobCategory ARTHROPOD = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("arthropod")));
    MobCategory UNDEAD = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("undead")));
    MobCategory SCULK = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("sculk")));
    /**
     * Used for entities that are purely considered objects.
     * Normal mobs probably do not want to attack these types of entities.
     */
    MobCategory OBJECT = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("object")));
    /**
     * Used for entities that are "ghostly".
     * Normal mobs probably do not want to attack these types of entities.
     */
    MobCategory ETERNAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("eternal")));
}
