package killercreepr.cruxstructures.structure.impl;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import killercreepr.crux.Crux;
import killercreepr.cruxstructures.event.StructurePlaceEvent;
import killercreepr.cruxstructures.structure.Structure;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;

public class FAWEStructure implements Structure {
    protected final @NotNull ClipboardHolder holder;
    public FAWEStructure(@NotNull ClipboardHolder holder) {
        this.holder = holder;
    }

    public FAWEStructure(@NotNull String filename) {
        File schematicFile = new File(WorldEdit.getInstance().getSchematicsFolderPath().toString() + "/" + filename + ".schem");
        if(!schematicFile.exists()) {
            throw new RuntimeException("Cannot find schematic file!");
        }
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if(format == null) {
            throw new RuntimeException("Invalid schematic format for schematic " + filename + "!");
        }

        try{
            Clipboard clipboard = format.load(schematicFile);
            this.holder = new ClipboardHolder(clipboard);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull StructurePlaceEvent place(@NotNull Location at) {
        StructurePlaceEvent event = new StructurePlaceEvent(this, at);
        if(!event.callEvent()) return event;

        pasteSchematic(at, true, true);

        return event;
    }

    public void pasteSchematic(@NotNull Location loc, boolean randomRotation, boolean ignoreAirBlocks){
        //paste schematic
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(loc.getWorld()))) {
            if(randomRotation){
                AffineTransform transform = new AffineTransform();
                transform = transform.rotateY(new Random().nextInt(4) * 90);
                holder.setTransform(holder.getTransform().combine(transform));
            }
            Operation operation = holder
                .createPaste(editSession)
                .to(BlockVector3.at(loc.getX(), loc.getY(), loc.getZ()))
                .ignoreAirBlocks(ignoreAirBlocks)
                .build();
            Operations.complete(operation);
        } catch (Exception e) {
            Crux.log(Level.WARNING, "Couldn't place clipboard at: '" + loc + "'.");
            e.printStackTrace();
        }
    }
}
