package killercreepr.cruxstructures.config.handler;

import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileSimpleLootTable;
import killercreepr.cruxstructures.structure.generation.StructureGenerator;

public class FileStructureHandlers {
    public static final FileSimpleLootTable<StructureGenerator> STRUCTURE_GENERATOR_LOOT_TABLE = new FileSimpleLootTable<>(StructureGenerator.class);
}
