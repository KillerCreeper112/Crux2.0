package killercreepr.cruxworlds.config;

import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxworlds.config.entity.CfgNaturalEntitySpawn;
import killercreepr.cruxworlds.config.handler.FileNaturalEntitySpawn;
import killercreepr.cruxworlds.config.handler.FileNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.config.handler.FileSpawnValidator;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;

public class CruxConfigsHook {
    public static final FileNaturalEntitySpawnGroup NATURAL_ENTITY_SPAWN_GROUP = new FileNaturalEntitySpawnGroup();
    public static final FileSpawnValidator SPAWN_VALIDATOR = new FileSpawnValidator();
    public static void register(){
        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxConfigsHook::registerHandlers);
    }
    public static void registerHandlers(FileRegistry registry){
        registry.registerFileHandler(CfgNaturalEntitySpawn.class, new FileNaturalEntitySpawn());
        registry.registerFileHandler(NaturalEntitySpawnGroup.class, NATURAL_ENTITY_SPAWN_GROUP);
        registry.registerFileHandler(SpawnValidator.class, SPAWN_VALIDATOR);
    }
}
