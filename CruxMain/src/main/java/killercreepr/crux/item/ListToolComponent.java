package killercreepr.crux.item;

import killercreepr.crux.block.CruxedBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ListToolComponent implements ToolComponent{
    protected final @NotNull List<ToolComponent> components;
    public ListToolComponent(@NotNull List<ToolComponent> components) {
        this.components = components;
    }

    @Override
    public @Nullable Result test(@NotNull CruxedBlock block) {
        for(ToolComponent c : components){
            Result result = c.test(block);
            if(result != null) return result;
        }
        return null;
    }
}
