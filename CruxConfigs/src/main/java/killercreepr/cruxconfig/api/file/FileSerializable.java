package killercreepr.cruxconfig.api.file;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;

public interface FileSerializable {
    FileElement serializeToFile(FileContext<?> ctx);
}
