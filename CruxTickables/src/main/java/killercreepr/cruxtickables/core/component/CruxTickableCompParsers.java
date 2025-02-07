package killercreepr.cruxtickables.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.component.CruxAttributeCompParsers;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableInstance;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.core.persistence.type.EntityTickablesComponentContainerDataType;
import killercreepr.cruxtickables.core.registries.CruxTickableRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CruxTickableCompParsers {
    public static PersistTextParser<EntityTickable> ENTITY_TICKABLE = PersistTextParser.elementBuilder(EntityTickable.class)
        .field(TextInputField.field(PersistTextParser.KEY, EntityTickable::key))
        .apply(ctx -> CruxTickableRegistries.ENTITY_TICKABLE.get(ctx.get()));

    public static PersistTextParser<EntityTickableModifier> ENTITY_TICKABLE_MODIFIER = PersistTextParser.mapBuilder(EntityTickableModifier.class)
        .field("key", TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .field("slot", TextInputField.field(CruxAttributeCompParsers.CRUX_SLOT_GROUP, e ->{
            if(e.getSlotGroup() == CruxSlotGroup.ANY) return null;
            return e.getSlotGroup();
        }))
        .field("path", TextInputField.field(PersistTextParser.LIST.KEY, e ->{
            Key[] path = e.getPath();
            if(path==null || path.length < 1) return null;
            return Arrays.asList(path);
        }))
        .apply(ctx ->{
            Key key = ctx.get("key");
            CruxSlotGroup slot = ctx.getOptional("slot");
            List<Key> setPath = ctx.getOptional("path");
            Key[] path;
            if(setPath == null || setPath.isEmpty()) path = null;
            else path = setPath.toArray(new Key[0]);
            return EntityTickableModifier.modifier(key, slot, path);
        });


    public static PersistTextParser<EntityTickableInstance> ENTITY_TICKABLE_INSTANCE = PersistTextParser.mapBuilder(EntityTickableInstance.class)
        .field("tickable", TextInputField.field(ENTITY_TICKABLE, EntityTickableInstance::getTickable))
        .field("modifiers", TextInputField.field(PersistTextParser.collectionList(ENTITY_TICKABLE_MODIFIER), EntityTickableInstance::getModifiers))
        .apply(ctx ->{
            EntityTickable attribute = ctx.get("tickable");
            Collection<EntityTickableModifier> modifiers = ctx.get("modifiers");
            return EntityTickableInstance.instance(attribute, modifiers);
        });

    public static PersistTextParser<EntityTickablesContainer> ENTITY_TICKABLES_CONTAINER = PersistTextParser.elementBuilder(EntityTickablesContainer.class)
        .field(TextInputField.field(PersistTextParser.collectionList(ENTITY_TICKABLE_INSTANCE), EntityTickablesContainer::getTickableInstances))
        .dataType(new EntityTickablesComponentContainerDataType())
        .apply(ctx ->{
            Collection<EntityTickableInstance> modifiers = ctx.get();
            return EntityTickablesContainer.container(modifiers);
        });
}
