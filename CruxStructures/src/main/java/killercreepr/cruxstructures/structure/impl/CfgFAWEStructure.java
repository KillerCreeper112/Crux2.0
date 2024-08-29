package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public class CfgFAWEStructure extends FAWEStructure{
    protected final boolean persistent;
    protected final @NotNull Collection<StructureModule> modules;
    public CfgFAWEStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent, @NotNull Collection<StructureModule> modules) {
        super(key, holder);
        this.persistent = persistent;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull String filename, boolean persistent, @NotNull Collection<StructureModule> modules) {
        super(key, filename);
        this.persistent = persistent;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent, @NotNull Collection<StructureModule> modules) {
        super(key, schematicFile);
        this.persistent = persistent;
        this.modules = modules;
    }

    @Override
    public @NotNull StructurePlaceEvent place(@NotNull Location at, double rotation) {
        StructurePlaceEvent event = super.place(at, rotation);
        if(event.isCancelled()) return event;
        modules.forEach(module -> module.onPlaced(this, at, rotation));
        return event;
    }

    public @NotNull Collection<StructureModule> getModules() {
        return modules;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }
}
