package killercreepr.cruxattributes.persistence;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxattributes.persistence.impl.CruxAttributeContainerDataType;
import killercreepr.cruxattributes.persistence.impl.CruxAttributeInstanceDataType;
import killercreepr.cruxattributes.persistence.impl.CruxAttributeModifierDataType;

public class CruxAttributesPersistence {
    public static void register(){}
    public static final CruxAttributeContainerDataType ATTRIBUTE_CONTAINER = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeContainerDataType());
    public static final CruxAttributeInstanceDataType ATTRIBUTE_INSTANCE = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeInstanceDataType());
    public static final CruxAttributeModifierDataType ATTRIBUTE_MODIFIER = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeModifierDataType());
}
