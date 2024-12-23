package killercreepr.cruxstructures.core.structure;

import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.data.world.StoredChunk;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import killercreepr.cruxstructures.api.structure.module.StructureBoundingBoxModule;
import killercreepr.cruxstructures.api.structure.module.StructureModule;
import killercreepr.cruxstructures.core.structure.stored.CfgStoredStructure;
import killercreepr.cruxstructures.core.util.CruxStructureUtil;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
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

    @Override
    public @Nullable StoredStructure buildStored(@NotNull Location center, double rotation) {
        CruxPosition cruxCenter = CruxPosition.block(center);
        StoredChunk chunk = StoredChunk.from(center);
        BoundingBox outerBox = CruxStructureUtil.calculateBoundingBox(cruxCenter, this, rotation);
        BoundingBox innerBox = null;
        for(StructureModule module : getModules()){
            if(module instanceof StructureBoundingBoxModule mod) innerBox = mod.editBoundingBox(this, center, rotation, innerBox == null ? outerBox : innerBox);
        }
        return new CfgStoredStructure(this.key(), chunk, cruxCenter, outerBox, rotation, innerBox);
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
