package killercreepr.cruxentities.entity;

import killercreepr.crux.core.Crux;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Keyed;

public interface MobCategory extends Keyed {
    static void register(){}

    MobCategory BOSS = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("boss")));
    MobCategory ELITE = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("elite")));
    /**
     * Represents mobs that are not aggressive in nature.
     */
    MobCategory NEUTRAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("neutral")));
    /**
     * Represents an animal-type mob such as pigs and cows.
     */
    MobCategory ANIMAL = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("animal")));
    /**
     * Represents mobs that are aggressive in nature.
     */
    MobCategory ENEMY = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("enemy")));
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
    MobCategory COSMETIC = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("cosmetic")));
    MobCategory HUMAN = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("human")));
    MobCategory PLAYER = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("player")));
    MobCategory FRIEND = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("friend")));
    MobCategory PET = CruxEntityRegistries.MOB_CATEGORY.register(new SimpleMobCategory(Crux.key("pet")));
}
