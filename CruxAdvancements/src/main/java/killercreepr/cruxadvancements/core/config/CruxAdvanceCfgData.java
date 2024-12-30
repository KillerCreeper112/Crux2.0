package killercreepr.cruxadvancements.core.config;

import killercreepr.cruxadvancements.core.config.handler.FileAdvancementObjective;

public class CruxAdvanceCfgData {
    private static final FileAdvancementObjective fileAdvancementObjective = new FileAdvancementObjective();
    public static FileAdvancementObjective fileAdvancementObjective(){
        return fileAdvancementObjective;
    }
}
