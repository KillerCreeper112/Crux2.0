package killercreepr.cruxblocks.core.config.loader;

import killercreepr.crux.core.Crux;
import killercreepr.cruxblocks.api.block.group.CruxBlockGroup;
import killercreepr.cruxblocks.core.config.handler.FileCruxBlockGroup;
import killercreepr.cruxblocks.core.registries.CruxBlocksRegistries;
import killercreepr.cruxconfig.config.bukkit.loader.CfgLoader;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

public class CruxBlockGroupLoader extends CfgLoader {
    protected final Collection<Key> registeredGroups = new HashSet<>();

    public Collection<Key> getRegisteredGroups() {
        return registeredGroups;
    }

    public void unregisterRegisteredGroups(){
        registeredGroups.forEach(CruxBlocksRegistries.BLOCK::unregisterGroup);
    }


    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        if(cfg.getElement("values") instanceof FileObject values){
            loadMultipleValues(new FileContext<>(cfg.fileRegistry()), values);
            return;
        }

        CruxBlockGroup table;
        if(path == null) table = cfg.deserialize("", CruxBlockGroup.class);
        else{
            if(!(cfg.getRoot() instanceof FileObject root)) return;

            FileContext<?> ctx = new FileContext<>(cfg.fileRegistry());
            table = FileCruxBlockGroup.deserialize(
                ctx, root, Crux.key(path)
            );
            if(table != null) table = ctx.getRegistry().getParsedObjectRegistry().parse(root, ctx, table);
        }
        if(table == null) return;
        CruxBlocksRegistries.BLOCK.registerGroup(table);
        registeredGroups.add(table.key());
        Crux.log(Level.INFO, "Registered block group: " + table.key());
    }

    public void loadMultipleValues(@NotNull FileContext<?> ctx, @NotNull FileObject values){
        values.forEach((key, value) ->{
            CruxBlockGroup item = FileCruxBlockGroup.deserialize(
                ctx, value, Crux.key(key)
            );
            if(item != null) item = ctx.getRegistry().getParsedObjectRegistry().parse(value, ctx, item);
            if(item == null) return;
            CruxBlocksRegistries.BLOCK.registerGroup(item);
            registeredGroups.add(item.key());
            Crux.log(Level.INFO, "Registered block group: " + item.key());
        });
    }
}
