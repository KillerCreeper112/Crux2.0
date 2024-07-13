package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.session.ClipboardHolder;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CfgFAWEStructure extends FAWEStructure{
    protected final boolean persistent;
    public CfgFAWEStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent) {
        super(key, holder);
        this.persistent = persistent;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull String filename, boolean persistent) {
        super(key, filename);
        this.persistent = persistent;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent) {
        super(key, schematicFile);
        this.persistent = persistent;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }
}
