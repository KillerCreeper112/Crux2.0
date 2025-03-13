package killercreepr.cruxmenus.api.menu.module.active;

public interface IActivePagedMenuModule<T> {
    IActivePagedMenuModule<T> addPage(int amount);
    IActivePagedMenuModule<T> setPage(int page);
    int getPage();
    int getMaxPage();
}
