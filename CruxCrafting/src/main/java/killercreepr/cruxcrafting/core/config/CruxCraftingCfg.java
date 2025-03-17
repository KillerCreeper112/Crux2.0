package killercreepr.cruxcrafting.core.config;

import killercreepr.crux.api.loot.conditions.LootCondition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.plugin.module.StandardModules;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.cruxadvancements.core.advancement.objective.ObjectiveCommonData;
import killercreepr.cruxadvancements.core.config.CruxAdvanceCfgData;
import killercreepr.cruxadvancements.core.config.handler.FileAdvancementObjective;
import killercreepr.cruxadvancements.core.config.handler.FileSimpleAdvanceObjective;
import killercreepr.cruxconfig.config.bukkit.handler.BukkitCfgHandlers;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.FileLootCondition;
import killercreepr.cruxconfig.config.bukkit.handler.impl.loot.SimpleFileLootCondition;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.registry.CfgRegistries;
import killercreepr.cruxcrafting.api.crafting.ingredient.CruxRecipeIngredient;
import killercreepr.cruxcrafting.core.advancement.objective.CruxCraftObjective;
import killercreepr.cruxcrafting.core.config.handler.*;
import killercreepr.cruxcrafting.core.loot.condition.CruxRecipeCondition;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CruxCraftingCfg {
    public static final FileSimpleCruxCraftingRecipe FILE_CRUX_CRAFTING_RECIPE = new FileSimpleCruxCraftingRecipe();
    public static final SimpleFileCruxRecipeIngredient FILE_CRUX_RECIPE_INGREDIENT = new SimpleFileCruxRecipeIngredient();

    public static void onLoad(){
        registerRecipes(FILE_CRUX_CRAFTING_RECIPE);
        registerRecipeIngredients(FILE_CRUX_RECIPE_INGREDIENT);

        CfgRegistries.SIMPLE_REGISTRY.forEach(CruxCraftingCfg::registerFileHandler);

        if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_CONFIGS)){
            loadLoot();
            if(CruxRegistries.MODULES.containsKey(StandardModules.CRUX_ADVANCEMENTS)){
                loadAdvancement();
            }
        }
    }

    public static void registerFileHandler(FileRegistry reg){
        reg.registerFileHandler(CruxRecipeIngredient.class, FILE_CRUX_RECIPE_INGREDIENT);
    }

    public static void registerRecipes(FileSimpleCruxCraftingRecipe file){
        file.registerHandler("shaped", new FileShapedCraftingRecipe());
        file.registerHandler("shapeless", new FileShapelessCraftingRecipe());
    }

    public static void registerRecipeIngredients(SimpleFileCruxRecipeIngredient file){
        file.registerHandler("any_of", new FileAnyOfIngredient());
    }

    private static final FileAdvancementObjective fileAdvancementObjective = CruxAdvanceCfgData.fileAdvancementObjective();
    public static void loadAdvancement() {
        fileAdvancementObjective.registerCustomHandler(new FileSimpleAdvanceObjective<>(Crux.key("crux_craft")) {
            @Override
            public @Nullable CruxCraftObjective deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull ObjectiveCommonData data) {
                Integer maxProgress = e.getObject(Integer.class, "amount");
                if(maxProgress==null) maxProgress = 1;
                return new CruxCraftObjective(data, maxProgress);
            }
        });
    }

    public static void loadLoot(){
        FileLootCondition file = BukkitCfgHandlers.LOOT_CONDITION;
        file.registerCustomHandler(new SimpleFileLootCondition<>(Crux.key("crux_recipe")) {
            @Override
            public @Nullable LootCondition deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e, @NotNull String target) {
                FileRegistry reg = ctx.getRegistry();
                return new CruxRecipeCondition(
                    target, reg.deserializeFromFile(Key.class, e.get("key"))
                );
            }
        });
    }
}
