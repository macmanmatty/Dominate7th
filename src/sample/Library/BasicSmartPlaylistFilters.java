package sample.Library;

import sample.Library.PlaylistFilter;

public enum BasicSmartPlaylistFilters {


    AllSongs(new PlaylistFilter()),


    None(null);



    BasicSmartPlaylistFilters(PlaylistFilter filter ) {
        this.filter = filter;

    }

    PlaylistFilter filter;// the corrosponding playlist filter for the enum


    public PlaylistFilter getFilter() {
        return filter;
    }


}



