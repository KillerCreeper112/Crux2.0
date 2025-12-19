package killercreepr.cruxentities.entity;

import io.papermc.paper.tag.EntitySetTag;
import io.papermc.paper.tag.EntityTags;
import killercreepr.crux.api.loot.LootContext;
import killercreepr.crux.api.loot.LootTable;
import killercreepr.crux.api.loot.bukkit.EventLootContexts;
import killercreepr.crux.api.text.context.InputContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.persistence.CruxPersist;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.util.CruxEntityUtil;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxentities.persistence.CruxEntitiesPersist;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface CruxMob extends Keyed {
    @NotNull Predicate<Entity> UNDESIRED_BEHAVIOR = e ->{
        if(!CruxEntityUtil.UNDESIRED_BEHAVIOR.test(e)) return false;
        return !CruxMob.isInCategory(e, MobCategory.OBJECT);
    };

    static boolean is(@NotNull Entity e, @NotNull CruxMob@NotNull... grim){
        for(CruxMob x : grim){
            if(is(e, x)) return true;
        }
        return false;
    }

    static boolean is(@NotNull Entity e){
        return CruxEntitiesPersist.ENTITY.has(e);
    }

    static boolean is(@NotNull Entity e, @NotNull CruxMob grim){
        return grim.key().equals(CruxEntitiesPersist.ENTITY.get(e, null));
    }

    static boolean isInAllCategories(@NotNull Entity e, @NotNull Collection<MobCategory> check) {
        Set<MobCategory> entityCategories = getCategories(e);
        for (MobCategory required : check) {
            if (!entityCategories.contains(required)) return false;
        }
        return true;
    }

    static boolean isInAllCategories(@NotNull CruxMob e, @NotNull Collection<MobCategory> check){
        Set<MobCategory> categories = e.getCategories();
        if(categories==null || categories.isEmpty()) return false;
        for (MobCategory required : check) {
            if (!categories.contains(required)) return false;
        }
        return true;
    }

    static boolean isInAllCategories(@NotNull Entity e, @NotNull MobCategory... check) {
        Set<MobCategory> entityCategories = getCategories(e);
        for (MobCategory required : check) {
            if (!entityCategories.contains(required)) return false;
        }
        return true;
    }

    static boolean isInAllCategories(@NotNull CruxMob e, @NotNull MobCategory... check){
        Set<MobCategory> categories = e.getCategories();
        if(categories==null || categories.isEmpty()) return false;
        for (MobCategory required : check) {
            if (!categories.contains(required)) return false;
        }
        return true;
    }

    static boolean isInCategory(@NotNull Entity e, @NotNull Collection<MobCategory> check){
        for (MobCategory category : getCategories(e)) {
            if (check.contains(category)) return true;
        }
        return false;
    }

    static boolean isInCategory(@NotNull CruxMob e, @NotNull Collection<MobCategory> check){
        Set<MobCategory> categories = e.getCategories();
        if(categories==null || categories.isEmpty()) return false;
        for (MobCategory category : categories) {
            if (check.contains(category)) return true;
        }
        return false;
    }

    static boolean isInCategory(@NotNull Entity e, @NotNull MobCategory... check){
        return isInCategory(e, Set.of(check));
    }

    static boolean isInCategory(@NotNull CruxMob e, @NotNull MobCategory... check){
        Set<MobCategory> categories = e.getCategories();
        if(categories==null || categories.isEmpty()) return false;
        Set<MobCategory> checkSet = Set.of(check);
        for (MobCategory category : categories) {
            if (checkSet.contains(category)) return true;
        }
        return false;
    }

    static Set<MobCategory> getCategories(@NotNull Entity e){
        CruxMob mob = get(e);
        if(mob==null) return getVanillaCategories(e);
        Set<MobCategory> cat = mob.getCategories();
        return cat == null ? getVanillaCategories(e) : cat;
    }

    static @NotNull Set<MobCategory> getVanillaCategories(@NotNull Entity e){
        Set<MobCategory> list = new HashSet<>();
        if(e instanceof Enemy) list.add(MobCategory.ENEMY);
        if(e instanceof Monster) list.add(MobCategory.MONSTER);
        if(e instanceof Animals) list.add(MobCategory.ANIMAL);
        if(EntityTags.UNDEADS.isTagged(e.getType())) list.add(MobCategory.UNDEAD);
        if(EntitySetTag.ENTITY_TYPES_ARTHROPOD.isTagged(e.getType())) list.add(MobCategory.ARTHROPOD);
        switch (e.getType()){
            case WARDEN -> list.add(MobCategory.SCULK);
        }
        if(e instanceof HumanEntity) list.add(MobCategory.HUMAN);
        if(e instanceof Player) list.add(MobCategory.PLAYER);
        return list;
    }

    static <T extends PersistentDataHolder> @Nullable Key getKey(@NotNull T e){
        return CruxEntitiesPersist.ENTITY.get(e);
    }

    static <T extends PersistentDataHolder> @Nullable CruxMob get(@NotNull T e){
        Key key = getKey(e);
        return key==null?null: CruxEntityRegistries.ENTITIES.get(key);
    }

    default void load(@NotNull Entity e){}
    default @NotNull Entity spawn(@NotNull Location at){
        return spawn(at, null);
    }
    @NotNull Entity spawn(@NotNull Location at, @Nullable Consumer<Entity> consumer);

    default void onDeath(@NotNull Entity e, @NotNull EntityDeathEvent event){
        event.getDrops().clear();
        int previousDroppedExp = event.getDroppedExp();
        event.setDroppedExp(0);

        LootContext ctx = null;

        Key key = key();
        Key lootKey = Key.key(key.namespace(), "entity/" + key.value());

        LootTable<ItemStack> lootTable = CruxRegistries.ITEM_LOOT_TABLE.get(lootKey);
        if(lootTable != null){
            ctx = ctx == null ? EventLootContexts.builder(event).build() : ctx;
            event.getDrops().addAll(lootTable.populateLoot(ctx));
        }

        LootTable<NumberProvider> numLoot = CruxRegistries.NUMBER_LOOT_TABLE.get(lootKey);
        if(numLoot != null){
            ctx = ctx == null ? EventLootContexts.builder(event).build() : ctx;
            List<NumberProvider> result = numLoot.populateLoot(ctx);
            if(!result.isEmpty()){
                int value = result.getFirst().sample(InputContext.simple(TagContainer.string().hook(e)))
                    .intValue();
                event.setDroppedExp(value);
            }
        }else{
            if(previousDroppedExp > 0) event.setDroppedExp(getDefaultDroppedExperience());
        }
    }

    default int getDefaultDroppedExperience(){
        if(isInCategory(this, MobCategory.ETERNAL, MobCategory.OBJECT, MobCategory.NEUTRAL)) return 0;
        if(isInCategory(this, MobCategory.ENEMY)) return CruxMath.random(10, 15);
        if(isInCategory(this, MobCategory.MONSTER)) return CruxMath.random(12, 15);
        if(isInCategory(this, MobCategory.ANIMAL)) return CruxMath.random(1, 3);
        return 0;
    }

    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }
    default @Nullable Set<MobCategory> getCategories(){
        return null;
    }
}
