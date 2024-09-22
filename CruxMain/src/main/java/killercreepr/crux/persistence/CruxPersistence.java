package killercreepr.crux.persistence;

import killercreepr.crux.persistence.impl.*;

public class CruxPersistence {
    public static final UUIDTagType UUID = new UUIDTagType();
    public static final PotionEffectTagType POTION_EFFECT = new PotionEffectTagType();
    public static final CruxKeyTagType CRUX_KEY = new CruxKeyTagType();
    public static final LocationTagType LOCATION = new LocationTagType();
    public static final VectorTagType VECTOR = new VectorTagType();
    public static final BlockPosTagType BLOCK_POS = new BlockPosTagType();
    public static final ToolComponentTagType TOOL_COMPONENT = new ToolComponentTagType();
    public static final ToolComponentRuleTagType TOOL_COMPONENT_RULE = new ToolComponentRuleTagType();

    public static final CruxListPersistence LIST = new CruxListPersistence();
}
