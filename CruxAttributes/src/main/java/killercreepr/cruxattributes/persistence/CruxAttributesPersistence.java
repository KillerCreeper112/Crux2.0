package killercreepr.cruxattributes.persistence;

import killercreepr.cruxattributes.persistence.impl.CruxAttributeContainerDataType;
import killercreepr.cruxattributes.persistence.impl.CruxAttributeInstanceDataType;
import killercreepr.cruxattributes.persistence.impl.CruxAttributeModifierDataType;

public class CruxAttributesPersistence {
    public static final CruxAttributeContainerDataType ATTRIBUTE_CONTAINER = new CruxAttributeContainerDataType();
    public static final CruxAttributeInstanceDataType ATTRIBUTE_INSTANCE = new CruxAttributeInstanceDataType();
    public static final CruxAttributeModifierDataType ATTRIBUTE_MODIFIER = new CruxAttributeModifierDataType();
}
