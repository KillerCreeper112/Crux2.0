package killercreepr.cruxblocks.core.config.handler;

import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxblocks.core.block.texture.NoteTextureData;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileNoteTextureData implements FileObjectHandler<NoteTextureData> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull NoteTextureData object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable NoteTextureData deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = ctx.getRegistry();
        Integer note = o.getObject(Integer.class, "note");
        Instrument instrument = registry.deserializeFromFile(Instrument.class, o.get("instrument"));
        if(CruxObjects.checkNull(note, instrument)) return null;

        boolean powered = o.getOrDefaultObject(Boolean.class, "powered", false);
        return new NoteTextureData(
            new Note(note), instrument, powered
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "note_texture_data";
    }
}
