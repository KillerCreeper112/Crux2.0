package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.crux.core.data.SimpleKeyed;
import killercreepr.cruxadvancements.api.advancement.objective.AdvancementObjective;
import net.kyori.adventure.key.Key;

public abstract class FileSimpleAdvanceObjective<T extends AdvancementObjective> extends SimpleKeyed implements CustomFileAdvancementObjective<T>{
    public FileSimpleAdvanceObjective(Key key) {
        super(key);
    }
}
