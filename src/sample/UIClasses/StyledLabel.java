package sample.UIClasses;

import javafx.scene.control.Button;
import javafx.scene.control.Label;


    public class StyledLabel extends Label {
       private  String styleSheetPath;
        private String styleName;


        public StyledLabel(String text, String styleSheetPath, String styleName) {
            super(text);
            getStyleClass().clear();
            getStyleClass().add(styleName);
            this.styleName=styleName;
            getStylesheets().add(styleSheetPath);
            this.styleSheetPath=styleSheetPath;
        }

        public StyledLabel(String styleSheetPath, String styleName) {

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


