package killercreepr.cruxadvancements.hook.menu;

import killercreepr.crux.Crux;
import killercreepr.crux.data.Holder;
import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.data.AdvancementTracker;
import killercreepr.cruxadvancements.data.TrackedAdvancement;
import killercreepr.cruxadvancements.data.entity.AdvancementHolder;
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
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
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
                return new SimplePagedMenuModule<CruxAdvancement>(id, indexes, valuesFilter, valueItems, emptyItems, this) {
                    @Override
                    public @NotNull Holder<List<CruxAdvancement>> getValues(@NotNull CfgMenu menu) {
                        return () ->{
                            CruxAdvancementManager<?> manager;
                            if(advancementManagerKey == null){
                                manager = menu.info().getOrThrow(CruxAdvancementManager.class);
                            }else manager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(advancementManagerKey);
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

        registry.register(new SimpleFilePagedCfg(fileMenuHolder, Crux.key("paged_advancements_sorted")) {
            @Override
            public @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx, @NotNull FileObject o,
                                                   @Nullable FileObject menuContext,
                                                   @NotNull String id,
                                                   @NotNull NumberProvider indexes,
                                                   @Nullable String valuesFilter,
                                                   @Nullable MenuItems valueItems,
                                                   @Nullable MenuItems emptyItems) {
                Key advancementManagerKey = ctx.getRegistry().deserializeFromFile(Key.class, o.get("advancement_manager"));
                return new SimplePagedMenuModule<CruxAdvancement>(id, indexes, valuesFilter, valueItems, emptyItems, this) {
                    @Override
                    public @NotNull Holder<List<CruxAdvancement>> getValues(@NotNull CfgMenu menu) {
                        return () ->{
                            CruxAdvancementManager<?> manager;
                            if(advancementManagerKey == null){
                                manager = menu.info().getOrThrow(CruxAdvancementManager.class);
                            }else manager = AdvancementRegistries.ADVANCEMENT_MANAGERS.get(advancementManagerKey);
                            if(manager == null){
                                Crux.log(Level.WARNING, "Cannot find advancement manager of " + advancementManagerKey + "!");
                                return List.of();
                            }

                            List<CruxAdvancement> list = new ArrayList<>(manager.getAdvancements().values());
                            list.removeIf(d -> d.parent() == null);
                            list.sort(new Comparator<>() {
                                @Override
                                public int compare(CruxAdvancement k1, CruxAdvancement k2) {
                                    int level1 = getLevel(k1);
                                    int level2 = getLevel(k2);

                                    return Integer.compare(level1, level2);
                                }

                                private int getLevel(CruxAdvancement advance) {
                                    int level = 0;
                                    CruxAdvancement advancement = advance;
                                    while (advancement != null) {
                                        Key parentKey = advancement.parent();
                                        if(parentKey == null || parentKey.equals(advancement.key())) break;
                                        level++;
                                        advancement = manager.getAdvancement(parentKey);
                                    }
                                    return level;
                                }
                            });

                            return list;
                        };
                    }
                };
            }

            @Override
            public @Nullable MenuModule parsePaged(@NotNull String id, @NotNull NumberProvider indexes, @Nullable String valuesFilter, @Nullable MenuItems valueItems, @Nullable MenuItems emptyItems) {
                return null;
            }
        });

        registry.register(new SimpleFilePagedCfg(fileMenuHolder, Crux.key("tracked_advancements")) {
            @Override
            public @Nullable MenuModule parsePaged(@NotNull FileContext<?> ctx, @NotNull FileObject o,
                                                   @Nullable FileObject menuContext,
                                                   @NotNull String id,
                                                   @NotNull NumberProvider indexes,
                                                   @Nullable String valuesFilter,
                                                   @Nullable MenuItems valueItems,
                                                   @Nullable MenuItems emptyItems) {
                String advancementManagerKeyUnparsed = ctx.getRegistry().deserializeFromFile(String.class, o.get("advancement_manager"));
                return new SimplePagedMenuModule<TrackedAdvancement>(id, indexes, valuesFilter, valueItems, emptyItems, this) {
                    @Override
                    public @NotNull Holder<List<TrackedAdvancement>> getValues(@NotNull CfgMenu menu) {
                        return () ->{
                            Player p = menu.info().getOrThrow("viewer", Player.class);
                            AdvancementHolder holder = EntityMemory.getOrCreateDataHolder(p, AdvancementHolder.class);
                            if(holder==null) return List.of();
                            AdvancementTracker tracker = holder.getAdvancementTracker();
                            List<TrackedAdvancement> list = new ArrayList<>();
                            Key advancementManagerKey = advancementManagerKeyUnparsed == null ? null :
                                Crux.key(
                                    menu.getHolder().getRegistry().getFormat().deserializeString(advancementManagerKeyUnparsed, menu.buildTags())
                                );
                            tracker.getTrackedAdvancements().forEach(tracked ->{
                                if(advancementManagerKey != null && !tracked.getManagerKey().equals(advancementManagerKey)) return;
                                list.add(tracked);
                            });
                            return list;
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
