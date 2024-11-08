package killercreepr.cruxentities.modelengine;

public enum ModelGenPhase {
    PRE_IMPORT,
    POST_IMPORT,
    PRE_ASSETS,
    POST_ASSETS,
    PRE_ZIPPING,
    POST_ZIPPING,
    FINISHED;

    public boolean hasFinished(){
        return this == FINISHED;
    }

    public boolean hasFinishedImport(){
        if(hasFinished()) return true;
        return switch (this){
            case POST_IMPORT, PRE_ASSETS, POST_ASSETS, PRE_ZIPPING, POST_ZIPPING -> true;
            default -> false;
        };
    }
}
