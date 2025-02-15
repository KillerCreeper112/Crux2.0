package killercreepr.cruxattributes.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.cruxattributes.api.attribute.*;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.attribute.map.SimpleCruxAttributeMap;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CruxAttributeCompParsers {
    public static PersistTextParser<CruxAttribute> CRUX_ATTRIBUTE = PersistTextParser.elementBuilder(CruxAttribute.class)
        .field(TextInputField.field(PersistTextParser.KEY, CruxAttribute::key))
        .apply(ctx -> CruxAttributeRegistries.ATTRIBUTES.get(ctx.get()));

    public static PersistTextParser<CruxSlot> CRUX_SLOT = PersistTextParser.elementBuilder(CruxSlot.class)
        .field(TextInputField.field(PersistTextParser.KEY, CruxSlot::key))
        .apply(ctx -> CruxAttributeRegistries.SLOT.get(ctx.get()));

    public static PersistTextParser<CruxSlotGroup> CRUX_SLOT_GROUP = PersistTextParser.elementBuilder(CruxSlotGroup.class)
        .field(TextInputField.field(PersistTextParser.KEY, CruxSlotGroup::key))
        .apply(ctx -> CruxAttributeRegistries.SLOT_GROUP.get(ctx.get()));

    public static PersistTextParser<CruxAttribute.Operation> CRUX_ATTRIBUTE_OPERATION = PersistTextParser.elementBuilder(CruxAttribute.Operation.class)
        .field(TextInputField.field(PersistTextParser.STRING, CruxAttribute.Operation::id))
        .apply(ctx -> CruxAttribute.Operation.match(ctx.get()));

    public static PersistTextParser<CruxAttributeModifier> CRUX_ATTRIBUTE_MODIFIER = PersistTextParser.mapBuilder(CruxAttributeModifier.class)
        .field("key", TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .field("amount", TextInputField.field(PersistTextParser.DOUBLE, CruxAttributeModifier::getAmount))
        .field("operation", TextInputField.field(CRUX_ATTRIBUTE_OPERATION, CruxAttributeModifier::getOperation))
        .field("slot", TextInputField.field(CRUX_SLOT_GROUP, e ->{
            if(e.getSlotGroup().equals(CruxSlotGroup.ANY)) return null; //don't save "any" slot group
            return e.getSlotGroup();
        }))
        .field("path", TextInputField.field(PersistTextParser.LIST.KEY, e ->{
            Key[] path = e.getPath();
            if(path == null) return null;
            return Arrays.asList(path);
        }))
        .apply(ctx ->{
            Key key = ctx.getOptional("key");
            double amount = ctx.get("amount");
            CruxAttribute.Operation operation = ctx.getOptional("operation", CruxAttribute.Operation.ADD);
            CruxSlotGroup slot = ctx.getOptional("slot");
            List<Key> path = ctx.getOptional("path");

            CruxAttributeModifier mod;
            if(key == null){
                mod = CruxAttributeModifier.baseModifier(amount, operation, slot);
            }else mod = CruxAttributeModifier.modifier(key, amount, operation, slot);

            if(path == null || path.isEmpty()) return mod;
            mod = mod.withPath(path.toArray(new Key[0]));
            return mod;
        });

    public static PersistTextParser<CruxAttributeInstance> CRUX_ATTRIBUTE_INSTANCE = PersistTextParser.mapBuilder(CruxAttributeInstance.class)
        .field("attribute", TextInputField.field(CRUX_ATTRIBUTE, CruxAttributeInstance::getAttribute))
        .field("modifiers", TextInputField.field(PersistTextParser.collectionList(CRUX_ATTRIBUTE_MODIFIER), CruxAttributeInstance::getModifiers))
        .field("amount", TextInputField.field(PersistTextParser.DOUBLE, e -> null))
        .apply(ctx ->{
            CruxAttribute attribute = ctx.get("attribute");
            Collection<CruxAttributeModifier> modifiers = ctx.getOptional("modifiers");

            if(modifiers == null){
                double amount = ctx.get("amount");
                return CruxAttributeInstance.instance(attribute, CruxAttributeModifier.baseModifier(amount));
            }

            return CruxAttributeInstance.instance(attribute, modifiers);
        });

    public static PersistTextParser<CruxAttributeContainer> CRUX_ATTRIBUTE_CONTAINER = PersistTextParser.elementBuilder(CruxAttributeContainer.class)
        .field(TextInputField.field(PersistTextParser.collectionList(CRUX_ATTRIBUTE_INSTANCE), CruxAttributeContainer::getAttributeInstances))
        .apply(ctx ->{
            Collection<CruxAttributeInstance> instances = ctx.get();
            return CruxAttributeContainer.container(instances);
        });

    public static PersistTextParser<CruxAttributeContainer> CRUX_ATTRIBUTES = PersistTextParser.elementBuilder(CruxAttributeContainer.class)
        .field(TextInputField.field(PersistTextParser.collectionList(CRUX_ATTRIBUTE_INSTANCE), CruxAttributeContainer::getAttributeInstances))
        .dataType(CruxAttributesPersistence.ATTRIBUTE_CONTAINER_COMPONENT)
        .apply(ctx ->{
            Collection<CruxAttributeInstance> instances = ctx.get();
            return CruxAttributeContainer.container(instances);
        });


    public static PersistTextParser<CruxAttributeMap> CRUX_ATTRIBUTE_MAP = PersistTextParser.mapDynamicBuilder(CruxAttributeMap.class)
        .keyParser(CRUX_ATTRIBUTE)
        .valueParser(PersistTextParser.DOUBLE)
        .mapToEncode(CruxAttributeMap::getMap)
        .apply(ctx ->{
            Map<CruxAttribute, Double> instances = ctx.get();
            return new SimpleCruxAttributeMap(instances);
        });
}
