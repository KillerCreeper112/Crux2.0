package killercreepr.cruxcore;

import killercreepr.crux.Crux;
import killercreepr.crux.CruxMainModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxModuleRegistry;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxadvancements.CruxAdvancementsModule;
import killercreepr.cruxattributes.CruxAttributesModule;
import killercreepr.cruxblocks.CruxBlocksModule;
import killercreepr.cruxblocks.registeries.CruxBlocksRegistries;
import killercreepr.cruxconfig.CruxConfigsModule;
import killercreepr.cruxconfig.config.bukkit.file.CruxFolder;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.loader.LootTableLoader;
import killercreepr.cruxcore.command.CruxCoreCommands;
import killercreepr.cruxcore.listener.PlayerDataListener;
import killercreepr.cruxenchants.CruxEnchantsModule;
import killercreepr.cruxentities.CruxEntitiesModule;
import killercreepr.cruxexternal.CruxExternalModule;
import killercreepr.cruxitems.CruxItemsModule;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxpotions.CruxPotionsModule;
import killercreepr.cruxstructures.CruxStructuresModule;
import killercreepr.cruxstructures.manager.StructureManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
///// OUTDATED
public class CruxCore extends CruxPlugin implements Listener {
    private static CruxCore instance;
    public static CruxCore inst(){ return instance; }
    protected final CruxModuleRegistry MODULES = CruxRegistries.MODULES;
    protected final CruxMainModule CRUX_MAIN = new CruxMainModule();
    protected final CruxItemsModule CRUX_ITEMS = new CruxItemsModule();
    protected final CruxMenusModule CRUX_MENUS = new CruxMenusModule();
    protected final CruxConfigsModule CRUX_CONFIGS = new CruxConfigsModule();
    protected final CruxPotionsModule CRUX_POTIONS = new CruxPotionsModule();
    protected final CruxAttributesModule CRUX_ATTRIBUTES = new CruxAttributesModule();
    protected final CruxEntitiesModule CRUX_ENTITIES = new CruxEntitiesModule();
    protected final CruxEnchantsModule CRUX_ENCHANTS = new CruxEnchantsModule();
    protected final CruxBlocksModule CRUX_BLOCKS = new CruxBlocksModule(CruxBlocksRegistries.BLOCKS);
    protected final CruxStructuresModule CRUX_STRUCTURES = new CruxStructuresModule();
    protected final CruxExternalModule CRUX_EXTERNAL = new CruxExternalModule();
    protected final CruxAdvancementsModule CRUX_ADVANCEMENTS = new CruxAdvancementsModule();

    public CruxExternalModule cruxExternal(){
        return CRUX_EXTERNAL;
    }
    public CruxAdvancementsModule cruxAdvancements(){
        return CRUX_ADVANCEMENTS;
    }
    public @NotNull CruxBlocksModule cruxBlocks(){
        return CRUX_BLOCKS;
    }
    public @NotNull CruxMenusModule cruxMenus(){ return CRUX_MENUS; }

    public CruxStructuresModule cruxStructures() {
        return CRUX_STRUCTURES;
    }

    public CruxModuleRegistry modules() {
        return MODULES;
    }

    public CruxMainModule cruxMain() {
        return CRUX_MAIN;
    }

    public CruxItemsModule cruxItems() {
        return CRUX_ITEMS;
    }

    public CruxConfigsModule cruxConfigs() {
        return CRUX_CONFIGS;
    }

    public CruxPotionsModule cruxPotions() {
        return CRUX_POTIONS;
    }

    public CruxAttributesModule cruxAttributes() {
        return CRUX_ATTRIBUTES;
    }

    public CruxEntitiesModule cruxEntities() {
        return CRUX_ENTITIES;
    }

    public CruxEnchantsModule cruxEnchants() {
        return CRUX_ENCHANTS;
    }

    protected final StructureManager structureManager = new StructureManager(this);

    public StructureManager structureManager() {
        return structureManager;
    }

    @Override
    public void onLoad() {
        instance = this;
        Crux.setMainPlugin(this);

        new CruxCoreCommands(this).register(this);

        CRUX_STRUCTURES.registerCommands(this, structureManager);

        BukkitCfgHandlers.initStandard();

        MODULES.register(
            CRUX_MAIN,
            CRUX_CONFIGS,
            CRUX_ITEMS,
            CRUX_MENUS,
            CRUX_POTIONS,
            CRUX_ATTRIBUTES,
            CRUX_ENTITIES,
            CRUX_ENCHANTS,
            CRUX_BLOCKS,
            CRUX_STRUCTURES,
            CRUX_EXTERNAL,
            CRUX_ADVANCEMENTS
        ).load(this);

        super.onLoad();
    }

    @Override
    public void enabled() {
        //enable modules.
        //they will automatically add in their listeners
        MODULES.enable(this);
        CRUX_ITEMS.registerGeneralDisplayFormatter();
        Crux.buildTickTask().runTaskTimer(this, 20L, 1L);

        CRUX_BLOCKS.buildBlockTickTask(getServer()).runTaskTimer(this, 20L, 1L);

        reload();
        registerListeners(
            this,
            structureManager,
            new PlayerDataListener()
        );
        structureManager.buildRunnable().runTaskTimer(this, 20L, 1L);
    }

    @Override
    public void disabled() {
        super.disabled();
        MODULES.unregisterAll(this);
        structureManager.saveAllWorlds();
    }

    @Override
    public void reload() {
        super.reload();
        //CRUX_CONFIGS.reload(this);
        MODULES.reload(this);

        CruxRegistries.PLUGIN.forEach(plugin ->{
            if(plugin instanceof CruxCore) return;
            plugin.reload(this);
        });

        new LootTableLoader().loadConfiguration(
            new CruxFolder(this, "loot_tables").file()
        );

        structureManager.loadConfiguration();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reload();
        return super.onCommand(sender, command, label, args);
    }
}
