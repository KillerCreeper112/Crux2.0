package killercreepr.cruxstructures.core.structure;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionBuilder;
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
import com.sk89q.worldedit.world.block.BlockState;
import killercreepr.crux.api.component.DataComponentHandler;
import killercreepr.crux.api.math.CruxPosition;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.math.BlockPos;
import killercreepr.cruxstructures.api.event.StructurePlaceEvent;
import killercreepr.cruxstructures.api.structure.Structure;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;
import java.util.logging.Level;

public class FAWEStructure extends DataComponentHandler.Simple implements Structure {
    protected final @NotNull Key key;
    protected final @NotNull ClipboardHolder holder;
    protected final @NotNull BoundingBox box;
    protected final @NotNull BlockPos originPos;
    public FAWEStructure(@NotNull Key key, @NotNull ClipboardHolder holder) {
        this.key = key;
        this.holder = holder;
        Clipboard clipboard = holder.getClipboards().getFirst();
        BlockVector3 min = clipboard.getMinimumPoint();
        BlockVector3 max = clipboard.getMaximumPoint();

        BlockVector3 origin = clipboard.getOrigin();
        originPos = new BlockPos(origin.x(), origin.y(), origin.z());
        box = new BoundingBox(
            min.x(), min.y(), min.z(),
            max.x(), max.y(), max.z()
        );
    }

    public FAWEStructure(@NotNull Key key, @NotNull String filename) {
        this(key, new File(WorldEdit.getInstance().getSchematicsFolderPath().toString() + "/" + filename + ".schem"));
    }

    public FAWEStructure(@NotNull Key key, @NotNull File schematicFile) {
        this.key = key;
        if(!schematicFile.exists()) {
            throw new RuntimeException("Cannot find schematic file at " + schematicFile.getAbsolutePath() + "!");
        }
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        if(format == null) {
            throw new RuntimeException("Invalid schematic format for schematic " + schematicFile.getName() + "!");
        }

        try{
            Clipboard clipboard = format.load(schematicFile);
            BlockVector3 min = clipboard.getMinimumPoint();
            BlockVector3 max = clipboard.getMaximumPoint();

            BlockVector3 origin = clipboard.getOrigin();
            originPos = new BlockPos(origin.x(), origin.y(), origin.z());

            box = new BoundingBox(
                min.x(), min.y(), min.z(),
                max.x(), max.y(), max.z()
            );
            this.holder = new ClipboardHolder(clipboard);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull StructurePlaceEvent place(@NotNull Location at, double rotation) {
        StructurePlaceEvent event = new StructurePlaceEvent(this, !Bukkit.isPrimaryThread(), at, rotation);
        if(!event.callEvent()) return event;

        pasteSchematic(at, event.getRotation(), true);

        return event;
    }

    @Override
    public @NotNull BoundingBox boundingBox() {
        return box;
    }

    @Override
    public @NotNull BlockPos originPos() {
        return originPos;
    }

    public void pasteSchematic(@NotNull Location loc, double rotation, boolean ignoreAirBlocks){
        //paste schematic
        try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder()
            .world(BukkitAdapter.adapt(loc.getWorld()))
            .changeSetNull()
            .fastMode(true)
            .build()) {
            AffineTransform transform = new AffineTransform();
            transform = transform.rotateY(rotation);
            ClipboardHolder holder = new ClipboardHolder(this.holder.getClipboards().getFirst());
            holder.setTransform(holder.getTransform().combine(transform));
            Operation operation = holder
                .createPaste(editSession)
                .to(BukkitAdapter.asBlockVector(loc))
                .ignoreAirBlocks(ignoreAirBlocks)
                .build();
            Operations.complete(operation);
        } catch (Exception e) {
            Crux.log(Level.WARNING, "Couldn't place clipboard at: '" + loc + "'.");
            e.printStackTrace();
        }
    }

    @Override
    @NotNull
    public Collection<CruxPosition> getBlocks(double rotation, @Nullable Predicate<CruxPosition> filter) {
        Collection<CruxPosition> list = new HashSet<>();
        Clipboard clipboard = holder.getClipboards().getFirst();
        clipboard.forEach(block ->{
            BlockState state = clipboard.getBlock(block);
            if(state.isAir()) return;
            CruxPosition pos = BlockPos.at(block.x(), block.y(), block.z()).rotateAroundY(
                originPos(), rotation
            );
            if(filter != null && !filter.test(pos)) return;

            list.add(pos);
        });
        return list;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }

    public @NotNull ClipboardHolder getHolder() {
        return holder;
    }

    public @NotNull BoundingBox getBox() {
        return box;
    }

    public @NotNull BlockPos getOriginPos() {
        return originPos;
    }
}
