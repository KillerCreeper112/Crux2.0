package killercreepr.cruxconfig.config.bukkit.handler.impl;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.entity.predicate.EntityPredicate;
import killercreepr.crux.api.entity.tag.EntityTag;
import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.entity.predicate.EntityAllPredicate;
import killercreepr.crux.core.entity.predicate.EntityAnyPredicate;
import killercreepr.crux.core.entity.predicate.EntityStringComponentPredicate;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

public class FileEntityPredicate extends SimpleFileHandler<EntityPredicate> {
    /*public final Codec<EntityPredicate> CODEC = PolymorphicCodecBuilder.polymorphicBuilder(EntityPredicate.class, "type")
      .register("all_of", new Codec<>() {
          @Override
          public EntityPredicate decode(DataNode node) throws DecodeException {
              if(!(node instanceof ObjectDataNode o)) return null;
              var values = o.get("values");
              if(values == null) return null;
              Collection<EntityPredicate> valueList = LIST_CODEC.decode(values);
              if(valueList==null) return null;

              return new EntityAllPredicate(valueList);
          }

          @Override
          public DataNode encode(EntityPredicate value) {
              return null;
          }
      })
      .register("any_of", new Codec<>() {
          @Override
          public EntityPredicate decode(DataNode node) throws DecodeException {
              if(!(node instanceof ObjectDataNode o)) return null;
              var values = o.get("values");
              if(values == null) return null;
              Collection<EntityPredicate> valueList = LIST_CODEC.decode(values);
              if(valueList==null) return null;

              return new EntityAnyPredicate(valueList);
          }

          @Override
          public DataNode encode(EntityPredicate value) {
              return null;
          }
      })
      .build();

    public final Codec<List<EntityPredicate>> LIST_CODEC = Codec.listCodec(CODEC);*/

    public final Map<String, Function<String, EntityPredicate>> NAMESPACED_TYPES = new HashMap<>();

    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull EntityPredicate object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable EntityPredicate deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        FileRegistry registry = ctx.getRegistry();
        if(e instanceof FileGeneric g){
            String key = g.getAsString();
            if(key.startsWith("#")){
                EntityTag tag = CruxRegistries.ENTITY_TAG.get(Crux.key(key.substring(1)));
                if(tag==null) return null;
                return EntityPredicate.fromTag(tag);
            }
            var split = key.split(":", 2);
            if(split.length > 1){
                var function = NAMESPACED_TYPES.get(split[0]);
                if(function != null){
                    return function.apply(split[1]);
                }
            }
            return EntityPredicate.fromType(Crux.key(key));
        }
        if(e instanceof FileArray a){
            Collection<EntityPredicate> children = new ArrayList<>();
            a.forEach(ele ->{
                EntityPredicate predicate = registry.deserializeFromFile(EntityPredicate.class, ele, ctx);
                if(predicate==null) return;
                children.add(predicate);
            });
            return EntityPredicate.fromAllOf(children);
        }
        if(!(e instanceof FileObject o)) return null;
        String type = o.getObject(String.class, "type");
        if(type == null) return null;
        return switch (type.toLowerCase()){
            case "any_of" ->{
                Collection<EntityPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<EntityPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new EntityAnyPredicate(values);
            }
            case "all_of" ->{
                Collection<EntityPredicate> values = registry.deserializeFromFile(
                    new TypeToken<Collection<EntityPredicate>>(){}.getType(),
                    o.get("values")
                );
                if(values==null) yield null;
                yield new EntityAllPredicate(values);
            }
            case "loot" ->{
                LootCondition loot = registry.deserializeFromFile(LootCondition.class, o.get("value"));
                if(loot == null) yield null;
                yield EntityPredicate.fromLootCondition(loot);
            }
            case "string_component" -> new EntityStringComponentPredicate(
              registry.deserializeFromFile(Key.class, o.get("component")),
              registry.deserializeFromFile(String.class, o.get("match")),
              registry.deserializeFromFileOrDefault(Boolean.class, o.get("ignore_case"), true)
            );
            default ->{
                Crux.log(Level.WARNING, "No entity predicate of " + type + " exists!");
                yield null;
            }
        };
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "entity_predicate";
    }
}
