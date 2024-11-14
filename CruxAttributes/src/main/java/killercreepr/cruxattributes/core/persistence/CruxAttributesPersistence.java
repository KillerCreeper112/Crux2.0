package killercreepr.cruxattributes.core.persistence;

import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxattributes.core.persistence.impl.CruxAttributeModifierDataType;
import killercreepr.cruxattributes.core.persistence.impl.CruxAttributeContainerDataType;
import killercreepr.cruxattributes.core.persistence.impl.CruxAttributeInstanceDataType;

public class CruxAttributesPersistence {
    public static void register(){}
    public static final CruxAttributeContainerDataType ATTRIBUTE_CONTAINER = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeContainerDataType());
    public static final CruxAttributeInstanceDataType ATTRIBUTE_INSTANCE = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeInstanceDataType());
    public static final CruxAttributeModifierDataType ATTRIBUTE_MODIFIER = CruxRegistries.PERSISTENT_DATA_TYPE.register(new CruxAttributeModifierDataType());
}
