package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.data.communication.CreateBlockSoundGroup;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.crux.data.communication.impl.SimpleCreateBlockSoundGroup;
import killercreepr.crux.util.CruxObjects;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCreateBlockSoundGroup implements FileObjectHandler<CreateBlockSoundGroup> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CreateBlockSoundGroup object) {
        FileRegistry registry = ctx.getRegistry();
        FileObject o = new FileObject();
        CreateSound sound = object.getBreakSound();
        if(sound != null) o.add("break", registry.serializeToFile(sound));
        sound = object.getPlaceSound();
        if(sound != null) o.add("place", registry.serializeToFile(sound));
        sound = object.getFallSound();
        if(sound != null) o.add("fall", registry.serializeToFile(sound));
        sound = object.getHitSound();
        if(sound != null) o.add("hit", registry.serializeToFile(sound));
        sound = object.getStepSound();
        if(sound != null) o.add("step", registry.serializeToFile(sound));
        return o;
    }

    @Override
    public @Nullable CreateBlockSoundGroup deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        CreateSound breakSound = registry.deserializeFromFile(CreateSound.class, o.get("break"));
        CreateSound placeSound = registry.deserializeFromFile(CreateSound.class, o.get("place"));
        CreateSound fallSound = registry.deserializeFromFile(CreateSound.class, o.get("fall"));
        CreateSound hitSound = registry.deserializeFromFile(CreateSound.class, o.get("hit"));
        CreateSound stepSound = registry.deserializeFromFile(CreateSound.class, o.get("step"));

        if(CruxObjects.checkAllNull(breakSound, placeSound, fallSound, hitSound, stepSound)) return null;
        return new SimpleCreateBlockSoundGroup(
            breakSound, stepSound, placeSound, hitSound, fallSound
        );
    }
}
