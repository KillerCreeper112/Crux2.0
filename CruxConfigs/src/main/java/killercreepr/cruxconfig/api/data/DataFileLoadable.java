package killercreepr.cruxconfig.api.data;

import killercreepr.cruxconfig.config.common.file.DataFile;

public interface DataFileLoadable {
    void save(DataFile file);
    void load(DataFile file);
}
