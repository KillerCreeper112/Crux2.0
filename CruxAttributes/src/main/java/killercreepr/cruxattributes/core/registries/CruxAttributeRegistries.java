package killercreepr.cruxattributes.core.registries;

import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.equipment.CruxSlot;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;

public class CruxAttributeRegistries {
    public static final KeyedRegistry<CruxAttribute> ATTRIBUTES = KeyedRegistry.keyedRegistry();
    public static final KeyedRegistry<CruxSlot> SLOT = KeyedRegistry.keyedRegistry();
    public static final KeyedRegistry<CruxSlotGroup> SLOT_GROUP = KeyedRegistry.keyedRegistry();
}
