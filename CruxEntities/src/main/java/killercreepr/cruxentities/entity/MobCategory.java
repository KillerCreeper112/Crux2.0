package killercreepr.cruxentities.entity;

import killercreepr.crux.Crux;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Keyed;

public interface MobCategory extends Keyed {
    MobCategory ANIMAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("animal")));
    MobCategory MONSTER = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("monster")));
    MobCategory ARTHROPOD = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("arthropod")));
    MobCategory UNDEAD = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("undead")));
    MobCategory SCULK = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("sculk")));
}
