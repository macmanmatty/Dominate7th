package sample;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;



public class AudioWindowPreLoader  extends Preloader {


    Stage preloaderStage;
    Label label= new Label("Loading Music Library...");

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage primaryStage)  {
        preloaderStage=primaryStage;
        preloaderStage.initStyle(StageStyle.DECORATED);

        VBox loading = new VBox(20);
       ImageView logo= new ImageView(new Image("/dominate7th.png"));
        HBox logoBox= new HBox(logo);
        logoBox.setAlignment(Pos.BASELINE_CENTER);

        loading.getStylesheets().add("css/preloader.css");
        loading.setMaxWidth(Region.USE_PREF_SIZE);
        loading.setMaxHeight(Region.USE_PREF_SIZE);
        loading.getChildren().add(logoBox);
        loading.getChildren().add(new ProgressBar());
        loading.getChildren().add(label);
        loading.setAlignment(Pos.BASELINE_CENTER);
        label.getStyleClass().add("title");

        BorderPane root = new BorderPane(loading);
        Scene scene = new Scene(root);

        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        if (info.getType() == StateChangeNotification.Type.BEFORE_START) {

            if ( preloaderStage.isShowing()) {
                //fade out, hide stage at the end of animation
                FadeTransition ft = new FadeTransition(
                        Duration.millis(20000), preloaderStage.getScene().getRoot());
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                final Stage s = preloaderStage;
                EventHandler<javafx.event.ActionEvent> eh = new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        s.hide();
                    }
                };
                ft.setOnFinished(eh);
                ft.play();
            } else {
                preloaderStage.hide();
            }
        }
    }

}



