package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.module.StructureModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class CfgFAWEStructure extends FAWEStructure{
    protected final boolean persistent;
    protected final @Nullable List<StructureModule> beforePlacementModules;
    protected final @NotNull List<StructureModule> modules;
    public CfgFAWEStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent, @Nullable List<StructureModule> beforePlacementModules, @NotNull List<StructureModule> modules) {
        super(key, holder);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull String filename, boolean persistent, @Nullable List<StructureModule> beforePlacementModules, @NotNull List<StructureModule> modules) {
        super(key, filename);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent, @Nullable List<StructureModule> beforePlacementModules, @NotNull List<StructureModule> modules) {
        super(key, schematicFile);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    @Override
    public @NotNull StructurePlaceEvent place(@NotNull Location at, double rotation) {
        if(beforePlacementModules != null){
            beforePlacementModules.forEach(module -> module.onPlaced(this, at, rotation));
        }
        StructurePlaceEvent event = super.place(at, rotation);
        if(event.isCancelled()) return event;
        modules.forEach(module -> module.onPlaced(this, at, rotation));
        return event;
    }

    public @NotNull List<StructureModule> getModules() {
        return modules;
    }

    public @Nullable List<StructureModule> getBeforePlacementModules() {
        return beforePlacementModules;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }
}
