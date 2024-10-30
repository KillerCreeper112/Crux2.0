package killercreepr.cruxconfig.config.bukkit.file;

import killercreepr.cruxconfig.config.common.file.DataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class BukkitDataFile {
    public static boolean isSupported(@NotNull File file){
        return CruxFolder.hasFileExtension(file, "yml") ||
            CruxFolder.hasFileExtension(file, "json");
    }
    public static @NotNull DataFile parse(@NotNull File file){
        if(CruxFolder.hasFileExtension(file, "yml")) return new CruxConfig(file);
        if(CruxFolder.hasFileExtension(file, "json")) return new CruxJson(file);
        throw new IllegalArgumentException(file.getName() + " is not a supported file type!");
    }
    public static @Nullable DataFile parseFromGeneralPath(@NotNull String absolutePath, @NotNull String fileName){
        DataFile dataFile;
        dataFile = attemptParse(new File(absolutePath, fileName));
        if(dataFile != null) return dataFile;

        fileName = CruxFolder.withoutFileExtension(fileName);
        dataFile = attemptParse(new File(absolutePath, fileName + ".yml"));
        if(dataFile != null) return dataFile;

        dataFile = attemptParse(new File(absolutePath, fileName + ".json"));
        return dataFile;
    }

    public static @Nullable DataFile parseFromGeneralPath(@NotNull File file){
        return parseFromGeneralPath(file.getParent(), file.getName());
    }

    private static DataFile attemptParse(File file){
        if(file.exists()){
            try{
                return parse(file);
            }catch (IllegalArgumentException ignored){}
        }
        return null;
    }
}
