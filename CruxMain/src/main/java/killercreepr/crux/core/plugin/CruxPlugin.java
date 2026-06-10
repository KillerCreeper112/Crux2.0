package killercreepr.crux.core.plugin;

import killercreepr.crux.api.plugin.module.CruxModule;
import killercreepr.crux.core.registries.CruxRegistries;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public abstract class CruxPlugin extends JavaPlugin implements CruxModule {
  /**
   * Use enabled() for onEnable logic.
   */
  @Override
  public final void onEnable() {
    super.onEnable();

    CruxRegistries.PLUGIN.register(this);

    enabled();
  }

  public void enabled() {
    reload();
  }

  @Override
  public @NotNull String name() {
    return this.getName();
  }

  /**
   * Use disabled() for onDisable logic.
   */
  @Override
  public final void onDisable() {
    super.onDisable();
    HandlerList.unregisterAll((Plugin) this);
    CruxRegistries.PLUGIN.unregister(this);
    disabled();
  }

  public void disabled() {
  }

  public void registerListeners(@NotNull Listener... listeners) {
    for (Listener l : listeners) {
      getServer().getPluginManager().registerEvents(l, this);
    }
  }

  public void reload() {
  }

  @Override
  public void reload(@NotNull CruxPlugin plugin) {
    CruxModule.super.reload(plugin);
    reload();
  }

  public CruxPlugin log(@NotNull Level level, @NotNull String text) {
    getLogger().log(level, text);
    return this;
  }

  public CruxPlugin log(@NotNull String info) {
    return log(Level.INFO, info);
  }

  public CruxPlugin logWarning(@NotNull String info) {
    return log(Level.WARNING, info);
  }

  public CruxPlugin logError(@NotNull String info) {
    return log(Level.SEVERE, info);
  }

  public void saveResource(String resourcePath){
    saveResource(resourcePath, null);
  }

  public void saveResource(String resourcePath, @Nullable Predicate<File> replaceExisting) {
    File destination = new File(getDataFolder(), resourcePath);

    boolean replace =
      destination.exists()
        && replaceExisting != null
        && replaceExisting.test(destination);

    if (destination.exists() && !replace) return;

    File parent = destination.getParentFile();
    if (parent != null) parent.mkdirs();

    try (InputStream input = getResource(resourcePath)) {
      if (input == null) throw new IllegalArgumentException("Resource '" + resourcePath + "' not found in jar.");

      Files.copy(
        input,
        destination.toPath(),
        StandardCopyOption.REPLACE_EXISTING
      );
    } catch (IOException e) {
      throw new RuntimeException("Failed to save resource '" + resourcePath + "'", e);
    }
  }

  public void saveResourceFolder(String folder){
    saveResourceFolder(folder, null);
  }

  public void saveResourceFolder(String folder, @Nullable Predicate<File> replaceExisting) {
    if (!folder.endsWith("/"))  folder += "/";

    try {
      File jarFile = new File(
        getClass()
          .getProtectionDomain()
          .getCodeSource()
          .getLocation()
          .toURI()
      );

      try (JarFile jar = new JarFile(jarFile)) {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
          JarEntry entry = entries.nextElement();

          String path = entry.getName();

          if (!path.startsWith(folder))  continue;

          if (entry.isDirectory())  continue;

          saveResource(path, replaceExisting);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to save resource folder '" + folder + "'", e);
    }
  }
}
