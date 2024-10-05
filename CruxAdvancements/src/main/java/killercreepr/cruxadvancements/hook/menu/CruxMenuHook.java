package killercreepr.cruxadvancements.hook.menu;

import killercreepr.crux.Crux;
import killercreepr.crux.data.Holder;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.hook.menu.data.AdvancementMenuDataParser;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import killercreepr.cruxadvancements.registries.AdvancementRegistries;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxmenus.CruxMenusModule;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.config.handler.FileMenuHolder;
import killercreepr.cruxmenus.api.menu.holder.MenuItems;
import killercreepr.cruxmenus.api.menu.module.MenuModule;
import killercreepr.cruxmenus.api.menu.module.config.MenuModuleBuilder;
import killercreepr.cruxmenus.api.menu.registry.MenuRegistry;
import killercreepr.cruxmenus.core.menu.module.standard.SimpleFilePagedCfg;
import killercreepr.cruxmenus.core.menu.module.standard.SimplePagedMenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CruxMenuHook {
    public static void register(@NotNull CruxMenusModule module){
        registerModules(module.menuRegistry().menuModule(), module.menuModuleRegistry());
        registerParsers(module.menuRegistry());
    }
    public static void registerParsers(MenuRegistry registry){
        registry.itemDataParsers().register(2, new AdvancementMenuDataParser(Crux.key("cruxadvancements")));
    }
    public static void registerModules(@NotNull FileMenuHolder<?> fileMenuHolder, @NotNull KeyedRegistry<MenuModuleBuilder> registry){
        registry.register(new SimpleFilePagedCfg(fileMenuHolder, Crux.key("paged_advancements")) {
            @Override
            public @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx, @NotNull FileObject o,
                                                   @Nullable FileObject menuContext,
                                                   @NotNull String id,
                                                   @NotNull NumberProvider indexes,
                                                   @Nullable String valuesFilter,
                                                   @Nullable MenuItems valueItems,
                                                   @Nullable MenuItems emptyItems) {
                Key advancementManagerKey = ctx.getRegistry().deserializeFromFile(Key.class, o.get("advancement_manager"));
                if(advancementManagerKey == null) return null;
                return new SimplePagedMenuModule<CruxAdvancement>(id, indexes, valuesFilter, valueItems, emptyItems, this) {
                    @Override
                    public @NotNull Holder<List<CruxAdvancement>> getValues(@NotNull CfgMenu menu) {
                        return () ->{
                            CruxAdvancementManager<?> manager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(advancementManagerKey);
                            if(manager == null){
                                Crux.log(Level.WARNING, "Cannot find advancement manager of " + advancementManagerKey + "!");
                                return List.of();
                            }
                            return new ArrayList<>(manager.getAdvancements().values());
                        };
                    }
                };
            }

            @Override
            public @Nullable MenuModule parsePaged(@NotNull String id, @NotNull NumberProvider indexes, @Nullable String valuesFilter, @Nullable MenuItems valueItems, @Nullable MenuItems emptyItems) {
                return null;
            }
        });
    }
}
