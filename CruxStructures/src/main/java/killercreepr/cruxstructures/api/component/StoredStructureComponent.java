package killercreepr.cruxstructures.api.component;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxstructures.api.structure.StoredStructure;
import org.jetbrains.annotations.NotNull;

public interface StoredStructureComponent {
    default void onFileSave(@NotNull FileContext<?> context, @NotNull FileObject o, @NotNull StoredStructure object){
    }
}
