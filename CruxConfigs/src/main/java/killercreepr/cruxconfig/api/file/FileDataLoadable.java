package killercreepr.cruxconfig.api.file;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;

public interface FileDataLoadable {
    void loadFromFile(FileContext<?> ctx, FileElement element);
}
