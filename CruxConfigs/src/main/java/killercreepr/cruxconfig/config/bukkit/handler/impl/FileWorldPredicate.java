package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.api.world.predicate.WorldPredicate;
import killercreepr.crux.core.Crux;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class FileWorldPredicate extends SimpleFileHandler<WorldPredicate> {
    protected final Map<Key, FileObjectHandler<WorldPredicate>> HANDLERS = new HashMap<>();
    public void registerHandler(Key key, FileObjectHandler<WorldPredicate> handler){
        HANDLERS.put(key, handler);
    }

    public Map<Key, FileObjectHandler<WorldPredicate>> getHandlers() {
        return HANDLERS;
    }

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull WorldPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable WorldPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                /*EntityTag tag = CruxRegistries.ENTITY_TAG.get(Crux.key(key.substring(1)));
                if(tag==null) return null;
                return WorldPredicate.fromTag(tag);*/
                throw new RuntimeException("WorldTag not implemented ATM");
            }
            return WorldPredicate.fromKey(Crux.key(key));
        }
        if(e instanceof FileArray a){
            Collection<WorldPredicate> children = new ArrayList<>();
            a.forEach(ele ->{
                WorldPredicate predicate = registry.deserializeFromFile(WorldPredicate.class, ele, ctx);
                if(predicate==null) return;
                children.add(predicate);
            });
            return WorldPredicate.fromAllOf(children);
        }
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type == null) return null;
        return switch (type.toLowerCase()){
            case "any_of" ->{
                Collection<WorldPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<EntityPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield WorldPredicate.fromAnyOf(values);
            }
            case "all_of" ->{
                Collection<WorldPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<WorldPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield WorldPredicate.fromAllOf(values);
            }
            case "loot" ->{
                LootCondition loot = registry.deserializeFromFile(LootCondition.class, o.get("value"));
                if(loot == null) yield null;
                yield WorldPredicate.fromLootCondition(loot);
            }
            default ->{
                FileObjectHandler<WorldPredicate> handler = HANDLERS.get(Crux.key(type));
                if(handler == null) Crux.log(Level.WARNING, "No world predicate of " + type + " exists!");
                else yield handler.deserializeFromFile(ctx, e);
                yield null;
            }
        };
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "world_predicate";
    }
}
