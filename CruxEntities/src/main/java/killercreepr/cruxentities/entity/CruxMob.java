package killercreepr.cruxentities.entity;

import io.papermc.paper.tag.EntitySetTag;
import io.papermc.paper.tag.EntityTags;
import killercreepr.crux.loot.LootContext;
import killercreepr.crux.loot.LootTable;
import killercreepr.crux.loot.bukkit.EventLootContexts;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.util.CruxString;
import killercreepr.cruxentities.persistence.CruxEntitiesPersist;
import killercreepr.cruxentities.registries.CruxEntityRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Consumer;

public interface CruxMob extends Keyed {
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

    static boolean isInAllCategories(@NotNull Entity e, @NotNull MobCategory... check){
        Collection<MobCategory> list = Arrays.asList(check);
        return Arrays.stream(getCategories(e)).allMatch(list::contains);
    }

    static boolean isInAllCategories(@NotNull CruxMob e, @NotNull MobCategory... check){
        MobCategory[] categories = e.getCategories();
        if(categories==null || categories.length == 0) return false;
        Collection<MobCategory> list = Arrays.asList(check);
        return Arrays.stream(categories).allMatch(list::contains);
    }

    static boolean isInCategory(@NotNull Entity e, @NotNull MobCategory... check){
        Collection<MobCategory> list = Arrays.asList(check);
        return Arrays.stream(getCategories(e)).anyMatch(list::contains);
    }

    static boolean isInCategory(@NotNull CruxMob e, @NotNull MobCategory... check){
        MobCategory[] categories = e.getCategories();
        if(categories==null || categories.length == 0) return false;
        Collection<MobCategory> list = Arrays.asList(check);
        return Arrays.stream(categories).anyMatch(list::contains);
    }

    static MobCategory[] getCategories(@NotNull Entity e){
        CruxMob mob = get(e);
        if(mob==null) return getVanillaCategories(e);
        MobCategory[] cat = mob.getCategories();
        return cat == null ? getVanillaCategories(e) : cat;
    }

    static @NotNull MobCategory[] getVanillaCategories(@NotNull Entity e){
        Collection<MobCategory> list = new HashSet<>();
        if(e instanceof Enemy) list.add(MobCategory.ENEMY);
        if(e instanceof Monster) list.add(MobCategory.MONSTER);
        if(e instanceof Animals) list.add(MobCategory.ANIMAL);
        if(EntityTags.UNDEADS.isTagged(e.getType())) list.add(MobCategory.UNDEAD);
        if(EntitySetTag.ENTITY_TYPES_ARTHROPOD.isTagged(e.getType())) list.add(MobCategory.ARTHROPOD);
        switch (e.getType()){
            case WARDEN -> list.add(MobCategory.SCULK);
        }
        return list.toArray(new MobCategory[0]);
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
        event.setDroppedExp(0);
        Key key = key();
        LootTable<ItemStack> lootTable = CruxRegistries.ITEM_LOOT_TABLE.get(Key.key(
            key.namespace(), "entity/" + key.value()
        ));
        if(lootTable==null) return;
        LootContext ctx = EventLootContexts.builder(event).build();
        event.getDrops().addAll(lootTable.populateLoot(ctx));
    }

    default @NotNull String getName(){
        return CruxString.toTitleCase(key().value());
    }
    default MobCategory[] getCategories(){
        return null;
    }
}
