package killercreepr.cruxworlds.config;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.PureYamlFileHandler;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxworlds.config.entity.CfgNaturalEntitySpawn;
import killercreepr.cruxworlds.config.handler.FileNaturalEntitySpawn;
import killercreepr.cruxworlds.config.handler.FileNaturalEntitySpawnGroup;
import killercreepr.cruxworlds.config.handler.FileSpawnValidator;
import killercreepr.cruxworlds.world.entity.NaturalEntitySpawnGroup;
import killercreepr.cruxworlds.world.spawning.SolidGroundSpawnValidator;
import killercreepr.cruxworlds.world.spawning.SpawnValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

        SPAWN_VALIDATOR.typeHandlers().register("solid_ground_2", new PureYamlFileHandler<SpawnValidator>() {
            @Override
            public @Nullable SpawnValidator deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
                return new SolidGroundSpawnValidator();
            }
        });
    }
}
