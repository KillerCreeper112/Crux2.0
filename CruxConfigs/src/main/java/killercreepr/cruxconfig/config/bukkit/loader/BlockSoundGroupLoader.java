package killercreepr.cruxconfig.config.bukkit.loader;

import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.file.DataFile;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;

public class BlockSoundGroupLoader extends CfgLoader {
    @Override
    public void loadConfiguration(@NotNull DataFile cfg, @Nullable String path){
        CreateBlockSoundGroup table;
        if(path == null) throw new UnsupportedOperationException("unsupported");
        if(!(cfg.getRoot() instanceof FileObject root)) return;
        Key key = Crux.key(path);

        table = cfg.fileRegistry().deserializeFromFile(
            CreateBlockSoundGroup.class, root
        );
        if(table == null) return;
        Crux.log(Level.INFO, "Registered global block sound group: " + key);
        CruxRegistries.BLOCK_SOUND_GROUP.register(key, table);
    }
}
