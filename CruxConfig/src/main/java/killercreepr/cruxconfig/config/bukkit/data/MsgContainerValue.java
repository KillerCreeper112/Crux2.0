package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.data.MsgContainer;
import org.jetbrains.annotations.Nullable;

public class MsgContainerValue extends CommonValue<MsgContainer> {
    public MsgContainerValue(@Nullable MsgContainer defaultValue) {
        super(MsgContainer.class, defaultValue);
    }

    public MsgContainerValue() {
        super(MsgContainer.class);
    }
}
