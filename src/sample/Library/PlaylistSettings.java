package sample.Library;

public class PlaylistSettings {
    boolean moveSongsToLibraryOnAdding;
    boolean addSongsToLibraryOnAdding;


    public boolean isMoveSongsToLibraryOnAdding() {
        return moveSongsToLibraryOnAdding;
    }

    public void setMoveSongsToLibraryOnAdding(boolean moveSongsToLibraryOnAdding) {
        this.moveSongsToLibraryOnAdding = moveSongsToLibraryOnAdding;
    }

    public boolean isAddSongsToLibraryOnAdding() {
        return addSongsToLibraryOnAdding;
    }

    public void setAddSongsToLibraryOnAdding(boolean addSongsToLibraryOnAdding) {
        this.addSongsToLibraryOnAdding = addSongsToLibraryOnAdding;
    }
}
