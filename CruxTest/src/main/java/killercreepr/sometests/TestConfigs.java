package killercreepr.sometests;

import killercreepr.cruxconfig.config.bukkit.data.CfgContainer;
import org.jetbrains.annotations.NotNull;

public class TestConfigs extends CfgContainer {
    public final @NotNull Config CFG;

    public TestConfigs(@NotNull Config CFG) {
        this.CFG = CFG;
    }
}
