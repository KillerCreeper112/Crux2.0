package killercreepr.cruxworlds.core.config.entity;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.entity.CruxEntitySnapshot;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.cruxworlds.api.component.EntitySpawnComponent;
import killercreepr.cruxworlds.api.world.entity.NaturalEntitySpawn;
import killercreepr.cruxworlds.api.world.entity.SpawnContext;
import killercreepr.cruxworlds.api.world.spawning.SpawnValidator;
import killercreepr.cruxworlds.core.component.CruxWorldsComponents;
import killercreepr.cruxworlds.core.component.EntitySpawnAttributes;
import killercreepr.cruxworlds.core.component.EntitySpawnPassengers;
import killercreepr.cruxworlds.core.world.entity.SimpleNaturalEntitySpawn;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CfgNaturalEntitySpawn extends SimpleNaturalEntitySpawn {
    protected final @NotNull CruxEntitySnapshot entitySnapshot;
    protected final @Nullable SpawnValidator spawnValidator;
    protected final DataExchange info;
    protected final DataComponentHandler components;
    public CfgNaturalEntitySpawn(int weight, float quality, @NotNull CruxEntitySnapshot entitySnapshot, @Nullable SpawnValidator spawnValidator,
                                 DataExchange info, DataComponentHandler components) {
        super(weight, quality);
        this.entitySnapshot = entitySnapshot;
        this.spawnValidator = spawnValidator;
        this.info = info;
        this.components = components;
    }

    @Override
    public void appendComponentsIfNotPresent(DataComponentAccessor components){
        components.forEach(typed ->{
            if(this.components.has(typed.getType())) return;
            this.components.set(typed);
        });
    }

    public @NotNull CruxEntitySnapshot getEntitySnapshot() {
        return entitySnapshot;
    }

    public @Nullable SpawnValidator getSpawnValidator() {
        return spawnValidator;
    }

    public DataExchange getInfo() {
        return info;
    }

    public DataComponentAccessor getComponents() {
        return components;
    }

    @Override
    public @Nullable Entity spawn(@NotNull SpawnContext ctx, @Nullable Consumer<Entity> consumer) {
        Location to = ctx.getPosition().toLocation(ctx.getWorld()).toCenterLocation().subtract(0, .4, 0);

        return entitySnapshot.createEntity(to, e ->{
            if(info.has("persistent")) e.setPersistent(info.getOrDefault("persistent", Boolean.class, false));
            if(info.has("remove_when_far_away") && e instanceof Mob m){
                m.setRemoveWhenFarAway(info.getOrDefault("remove_when_far_away", Boolean.class, false));
            }

            var textCtx = TextParserContext.tags(TagContainer.merged().hook(e));

            components.forEachAllOfTypeDeep(EntitySpawnComponent.class, c -> c.onCreateEntity(ctx, e, textCtx));

            /*EntitySpawnPassengers passengers = components.get(CruxWorldsComponents.ENTITY_SPAWN_PASSENGERS);
            if(passengers != null){
                Entity lastPassenger = null;
                for (NaturalEntitySpawn spawn : passengers.passengers) {
                    Entity spawned = spawn.spawn(ctx);
                    if(spawned == null) return;

                    if(passengers.append && lastPassenger != null){
                        lastPassenger.addPassenger(spawned);
                    }else e.addPassenger(spawned);

                    lastPassenger = spawned;
                }
            }*/

            /*EntitySpawnAttributes attributes = components.get(CruxWorldsComponents.ENTITY_SPAWN_ATTRIBUTES);
            if(attributes != null && e instanceof LivingEntity living){
                TextParserContext context = TextParserContext.empty();
                attributes.attributes.forEach((keyObject, modObject) ->{
                    NamespacedKey key = NamespacedKey.fromString(context.deserializeString(keyObject.toString()));
                    if(key==null) return;

                    Attribute attribute = Registry.ATTRIBUTE.get(key);
                    if(attribute == null) return;
                    AttributeInstance instance = living.getAttribute(attribute);
                    if(instance == null) return;

                    modObject.forEach(mod ->{
                        AttributeModifier m = mod.buildModifier(attribute, context);
                        instance.addModifier(m);
                    });
                });
            }*/

            if(consumer != null) consumer.accept(e);
        });
    }

    @Override
    public boolean canSpawn(@NotNull SpawnContext ctx) {
        return spawnValidator == null || spawnValidator.canSpawn(ctx);
    }

    @Override
    public @NotNull DataExchange info() {
        return info;
    }
}
