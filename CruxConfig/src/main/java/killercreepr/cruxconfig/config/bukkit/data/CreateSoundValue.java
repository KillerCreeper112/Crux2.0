package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.data.CreateSound;
import org.jetbrains.annotations.Nullable;

public class CreateSoundValue extends CommonValue<CreateSound> {
    public CreateSoundValue(@Nullable CreateSound defaultValue) {
        super(CreateSound.class, defaultValue);
    }

    public CreateSoundValue() {
        super(CreateSound.class);
    }
}
