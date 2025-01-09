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
        return parseFromGeneralPath(absolutePath, fileName, false);
    }
    public static @Nullable DataFile parseFromGeneralPath(@NotNull String absolutePath, @NotNull String fileName, boolean createIfNeeded){
        DataFile dataFile;
        dataFile = attemptParse(new File(absolutePath, fileName), createIfNeeded);
        if(dataFile != null) return dataFile;

        fileName = CruxFolder.withoutFileExtension(fileName);
        dataFile = attemptParse(new File(absolutePath, fileName + ".yml"), createIfNeeded);
        if(dataFile != null) return dataFile;

        dataFile = attemptParse(new File(absolutePath, fileName + ".json"), createIfNeeded);
        return dataFile;
    }

    public static @Nullable DataFile parseFromGeneralPath(@NotNull File file){
        return parseFromGeneralPath(file, false);
    }

    public static @Nullable DataFile parseFromGeneralPath(@NotNull File file, boolean createIfNeeded){
        return parseFromGeneralPath(file.getParent(), file.getName(), createIfNeeded);
    }

    private static DataFile attemptParse(File file, boolean createIfNeeded){
        if(createIfNeeded || file.exists()){
            try{
                return parse(file);
            }catch (IllegalArgumentException ignored){}
        }
        return null;
    }
}
