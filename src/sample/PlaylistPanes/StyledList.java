package sample.PlaylistPanes;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


public class   StyledList<T> extends ListView<T> {
   private  String styleSheetPath;
   private String styleName;

    public StyledList(ObservableList<T> items, String styleSheetPath, String styleName) {
        super(items);
        getStyleClass().clear();
        getStyleClass().add(styleName);
        this.styleName=styleName;
        getStylesheets().add(styleSheetPath);
        this.styleSheetPath=styleSheetPath;

    }

    public StyledList(String styleSheetPath, String styleName) {

        getStyleClass().clear();
        getStyleClass().add(styleName);
        this.styleName=styleName;
        getStylesheets().add(styleSheetPath);
        this.styleSheetPath=styleSheetPath;

    }

    public String getStyleSheetPath() {
        return styleSheetPath;
    }

    public void setStyleSheetPath(String styleSheetPath) {
        this.styleSheetPath = styleSheetPath;
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }
}


