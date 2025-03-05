package killercreepr.cruxtickables.core.component;

import killercreepr.crux.api.component.parser.InputDecodeContext;
import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.parser.hybrid.text.MapPersistTextParser;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.component.CruxAttributeCompParsers;
import killercreepr.cruxtickables.api.entity.tickable.DataEntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
import killercreepr.cruxtickables.api.equipment.SetBonus;
import killercreepr.cruxtickables.core.equipment.MainSetBonus;
import killercreepr.cruxtickables.core.equipment.SubSetBonus;
import killercreepr.cruxtickables.core.persistence.EntityTickableModifierDataType;
import killercreepr.cruxtickables.core.registries.CruxTickableRegistries;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;

import java.util.Collection;
import java.util.Map;

public class CruxTickableCompParsers {
    public static PersistTextParser<SetBonus> SET_BONUS = PersistTextParser.mapBuilder(SetBonus.class)
        .field("key", TextInputField.field(PersistTextParser.KEY, SetBonus::key))
        .field("equipment_amount", TextInputField.field(PersistTextParser.INTEGER, e ->{
            if(e.isMain()) return e.getEquipmentAmount();
            return null;
        }))
        .apply(ctx ->{
            Key key = ctx.get("key");
            if(key==null) return null;
            int equipment = ctx.getOptional("equipment_amount", 0);
            if(equipment < 1) return new SubSetBonus(key);
            return new MainSetBonus(key, equipment);
        });

    public static PersistTextParser<EntityTickable> ENTITY_TICKABLE = PersistTextParser.elementBuilder(EntityTickable.class)
        .field(TextInputField.field(PersistTextParser.KEY, EntityTickable::key))
        .apply(ctx -> CruxTickableRegistries.ENTITY_TICKABLE.get(ctx.get()));

    public static PersistTextParser<EntityTickableModifier> ENTITY_TICKABLE_MODIFIER = PersistTextParser.mapBuilder(EntityTickableModifier.class)
        .field("key", TextInputField.field(PersistTextParser.KEY, Keyed::key))
        .field("tickable", TextInputField.field(ENTITY_TICKABLE, EntityTickableModifier::getTickable))
        .field("slot", TextInputField.field(CruxAttributeCompParsers.CRUX_SLOT_GROUP, e ->{
            if(e.getSlotGroup() == CruxSlotGroup.ANY) return null;
            return e.getSlotGroup();
        }))
        .field("set_bonus", TextInputField.field(SET_BONUS, EntityTickableModifier::getSetBonus))
        .field("data", TextInputField.field(PersistTextParser.mapBuilder()
            .resultParser(InputDecodeContext::get)
            .buildUnset(), EntityTickableModifier::getData))
        .dataTypeFunction(parser ->{
            MapPersistTextParser<EntityTickableModifier> mapParser = (MapPersistTextParser<EntityTickableModifier>) parser;
            return new EntityTickableModifierDataType(
                EntityTickableModifier.class, mapParser.getElements(), mapParser
            );
        })
        .apply(ctx ->{
            Key key = ctx.getOptional("key", Crux.key("base"));
            EntityTickable tickable = ctx.get("tickable");
            CruxSlotGroup slot = ctx.getOptional("slot");
            SetBonus setBonus = ctx.getOptional("set_bonus");

            if(ctx.getOptional("data") instanceof Map<?,?> data && !data.isEmpty() && tickable instanceof DataEntityTickable dataTickable){
                Object dataObject = dataTickable.getDataParser().decodeObject(data);
                return EntityTickableModifier.modifier(key, tickable, slot, setBonus, dataObject);
            }
            return EntityTickableModifier.modifier(key, tickable, slot, setBonus);
        });

    /*public static PersistTextParser<EntityTickableInstance> ENTITY_TICKABLE_INSTANCE = PersistTextParser.mapBuilder(EntityTickableInstance.class)
        .field("tickable", TextInputField.field(ENTITY_TICKABLE, EntityTickableInstance::getTickable))
        .field("modifiers", TextInputField.field(PersistTextParser.collectionList(ENTITY_TICKABLE_MODIFIER), EntityTickableInstance::getModifiers))
        .apply(ctx ->{
            EntityTickable attribute = ctx.get("tickable");
            Collection<EntityTickableModifier> modifiers = ctx.get("modifiers");
            return EntityTickableInstance.instance(attribute, modifiers);
        });*/

    public static PersistTextParser<EntityTickablesContainer> ENTITY_TICKABLES_CONTAINER = PersistTextParser.elementBuilder(EntityTickablesContainer.class)
        .field(TextInputField.field(PersistTextParser.collectionList(ENTITY_TICKABLE_MODIFIER), EntityTickablesContainer::getTickableModifiers))
        .apply(ctx ->{
            Collection<EntityTickableModifier> modifiers = ctx.get();
            return EntityTickablesContainer.container(modifiers);
        });
}
