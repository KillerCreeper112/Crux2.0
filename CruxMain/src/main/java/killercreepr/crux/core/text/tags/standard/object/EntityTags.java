package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.HookedObjectContainer;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.persistence.PersistTag;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.hook.StringHookedObjectTag;
import killercreepr.crux.core.text.hook.StringListHookedObjectTag;
import killercreepr.crux.core.text.resolver.Tag;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityTags implements ObjectTag<Entity> {
    @Override
    public @NotNull Class<Entity> getObjectType() {
        return Entity.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("entity_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull Entity p, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("name", (args, context) -> p.getName() + ""))
            .add(Tag.string("x", (args, ctx) -> p.getX() + ""))
            .add(Tag.string("y", (args, ctx) -> p.getY() + ""))
            .add(Tag.string("z", (args, ctx) -> p.getZ() + ""))
            .add(Tag.string("crux_persist_tag", (args, ctx) ->{
                String tagID = ctx.deserializeString(args.get(0));
                PersistTag<?> tag = PersistTag.REGISTRY.get(tagID);
                if(tag == null) return "persisttag " + tagID + " not found";
                return tag.get(p) + "";
            }))
            .add(Tag.string("crux_component", (args, ctx) ->{
                Key key = Crux.key(ctx.deserializeString(args.get(0)));
                var type = CruxRegistries.DATA_COMPONENT_TYPE.get(key);
                if(type == null) return "datacomponenttype " + key + " not found";
                CruxEntity crux = CruxEntity.entity(p);
                var got = crux.get(type);
                if(got == null) return args.has(1) ? ctx.deserializeString(args.get(1)) : "null";
                return got + "";
            }))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringHookedObjectTag<?>> hookStrings(@NotNull Entity object, Object base, Object parent, @NotNull TagParser tags) {
        return HookedObjectContainer.string()
            .addAll(tags.hookStrings(object.getWorld(), base, object, FormatPrefix.simple("world/")))
            ;
    }

    @Override
    public @Nullable HookedObjectContainer<StringListHookedObjectTag<?>> hookStringLists(@NotNull Entity object, Object base, Object parent, @NotNull TagParser tags) {
        return HookedObjectContainer.stringList()
            .addAll(tags.hookStringLists(object.getWorld(), base, object, FormatPrefix.simple("world/")))
            ;
    }
}
