package killercreepr.cruxconfig.config.bukkit.standard;

import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.communication.lang.LangProvider;
import killercreepr.crux.api.data.Holder;
import killercreepr.crux.core.communication.processor.LangProcessor;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class SimpleLangConfig extends LangConfig implements LangProvider {
    public final Holder<CreateLang> lang;
    protected final Class<?> langClass;

    public SimpleLangConfig(@NotNull Plugin plugin, @NotNull String path, Holder<CreateLang> lang, Class<?> langClass) {
        super(plugin, path);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public SimpleLangConfig(@NotNull File file, Holder<CreateLang> lang, Class<?> langClass) {
        super(file);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public SimpleLangConfig(@NotNull CruxConfig cfg, Holder<CreateLang> lang, Class<?> langClass) {
        super(cfg);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public SimpleLangConfig(@NotNull Plugin plugin, @NotNull String path, @NotNull YamlRegistry registry, Holder<CreateLang> lang, Class<?> langClass) {
        super(plugin, path, registry);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public SimpleLangConfig(@NotNull File file, @NotNull YamlRegistry registry, Holder<CreateLang> lang, Class<?> langClass) {
        super(file, registry);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public SimpleLangConfig(@NotNull CruxConfig cfg, @NotNull YamlRegistry registry, Holder<CreateLang> lang, Class<?> langClass) {
        super(cfg, registry);
        this.lang = lang;
        this.langClass = langClass;
        process();
    }

    public void process(){
        LangProcessor.process(langClass);
    }

    @Override
    public void reload(@NotNull CruxPlugin plugin) {
        if(!file.exists()){
            setDefaults(langClass).save();
        }
        super.reload();
        populate(lang());
    }

    @Override
    public @NotNull CreateLang lang() {
        return lang.value();
    }
}