package killercreepr.cruxstructures.core.structure;

import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.cruxstructures.api.component.StructureComponent;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.api.structure.CfgStructure;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class CfgFAWEStructure extends FAWEStructure implements CfgStructure {
    protected final boolean persistent;
    protected final @Nullable List<StructureComponent> beforePlacementModules;
    protected final @NotNull List<StructureComponent> modules;
    public CfgFAWEStructure(@NotNull Key key, @NotNull ClipboardHolder holder, boolean persistent, @Nullable List<StructureComponent> beforePlacementModules, @NotNull List<StructureComponent> modules) {
        super(key, holder);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull String filename, boolean persistent, @Nullable List<StructureComponent> beforePlacementModules, @NotNull List<StructureComponent> modules) {
        super(key, filename);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    public CfgFAWEStructure(@NotNull Key key, @NotNull File schematicFile, boolean persistent, @Nullable List<StructureComponent> beforePlacementModules, @NotNull List<StructureComponent> modules) {
        super(key, schematicFile);
        this.persistent = persistent;
        this.beforePlacementModules = beforePlacementModules;
        this.modules = modules;
    }

    @Override
    public void load(){
        for(StructureComponent c : modules){
            c.onStructureHook(this);
        }
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

    /*@Override
    public @Nullable StoredStructure buildStored(@NotNull Location center, double rotation) {
        *//*CruxPosition cruxCenter = CruxPosition.block(center);
        StoredChunk chunk = StoredChunk.from(center);
        StoredStructure stored = new SimpleStoredStructure(this, chunk, cruxCenter, rotation);
        return stored;*//*
        *//*BoundingBox innerBox = CruxStructureUtil.calculateBoundingBox(cruxCenter, this, rotation);
        BoundingBox outerBox = null;
        for(StructureModule module : getModules()){
            if(module instanceof StructureBoundingBoxModule mod) outerBox = mod.editBoundingBox(this, center, rotation, outerBox == null ? innerBox : outerBox);
        }*//*
        //return new CfgStoredStructure(this.key(), chunk, cruxCenter, outerBox == null ? innerBox : outerBox, rotation, innerBox);
    }*/

    public @NotNull List<StructureComponent> getModules() {
        return modules;
    }

    public @Nullable List<StructureComponent> getBeforePlacementModules() {
        return beforePlacementModules;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }
}
