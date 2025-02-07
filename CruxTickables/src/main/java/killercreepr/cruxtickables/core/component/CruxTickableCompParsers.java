package killercreepr.cruxtickables.core.component;

import killercreepr.crux.api.component.parser.hybrid.PersistTextParser;
import killercreepr.crux.api.component.parser.hybrid.TextInputField;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeContainer;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.component.CruxAttributeCompParsers;
import killercreepr.cruxattributes.core.persistence.CruxAttributesPersistence;
import killercreepr.cruxattributes.core.registries.CruxAttributeRegistries;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickable;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickableModifier;
import killercreepr.cruxtickables.api.entity.tickable.EntityTickablesContainer;
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
        .field("tickable", TextInputField.field(ENTITY_TICKABLE, EntityTickableModifier::getTickable))
        .field("slot", TextInputField.field(CruxAttributeCompParsers.CRUX_SLOT_GROUP, e ->{
            if(e.getSlotGroup() == CruxSlotGroup.ANY) return null;
            return e.getSlotGroup();
        }))
        .apply(ctx ->{
            EntityTickable tickable = ctx.get("tickable");
            CruxSlotGroup slot = ctx.getOptional("slot");
            return EntityTickableModifier.modifier(tickable, slot);
        });

    public static PersistTextParser<EntityTickablesContainer> ENTITY_TICKABLES_CONTAINER = PersistTextParser.mapBuilder(EntityTickablesContainer.class)
        .field("modifiers", TextInputField.field(PersistTextParser.collectionList(ENTITY_TICKABLE_MODIFIER), EntityTickablesContainer::getTickableModifiers))
        .apply(ctx ->{
            Collection<EntityTickableModifier> modifiers = ctx.get("modifiers");
            return EntityTickablesContainer.container(modifiers);
        });
}
