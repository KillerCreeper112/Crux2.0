package killercreepr.cruxattributes.core.config;

import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeHandler;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.attribute.DynamicCruxAttributeInstance;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxattributes.core.config.handler.*;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;

public class CruxAttributesCfgHook {
    public static void onLoad(){
        for(FileRegistry reg : CfgRegistries.SIMPLE_REGISTRY){
            registerHandlers(reg);
        }
    }

    public static void registerHandlers(FileRegistry reg){
        reg.registerFileHandler(CruxAttribute.class, new FileCruxAttribute());
        reg.registerFileHandler(CruxAttributeHandler.class, new FileCruxAttributeHandler());
        reg.registerFileHandler(CruxAttributeModifier.class, new FileCruxAttributeModifier());
        reg.registerFileHandler(CruxAttribute.Operation.class, new FileCruxAttributeOperation());
        reg.registerFileHandler(CruxSlotGroup.class, new FileCruxSlotGroup());
        reg.registerFileHandler(DynamicCruxAttributeInstance.class, new FileDynamicCruxAttributeInstance());
    }
}
