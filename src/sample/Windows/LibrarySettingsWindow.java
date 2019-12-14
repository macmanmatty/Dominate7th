package sample.Windows;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import sample.MainAudioWindow;
import sample.Library.MusicLibrary;
import sample.Library.Settings;

import java.io.File;

public class LibrarySettingsWindow {
    Stage stage;
    MainAudioWindow window;
    Button setLibarary;
    Button reScanFolder;
    Button setFpCalcPath;

    CheckBox moveFilesToLibraryFolder;
    CheckBox getMissingTagsOnAdding;
    CheckBox organizeLibraryByFileType;
    CheckBox organizeLibraryByBitRate;
    CheckBox scanLibraryForNewSongs;
    CheckBox showNotificationsOnSongChange;
    CheckBox showNotificationsOnProcessComplete;
    CheckBox showProcessNotifications;
    CheckBox saveLibraryOnExit;
    CheckBox saveLibraryOnEdits;
    CheckBox copyFilesToLibraryFolder;
    CheckBox addAllSongsToLibrary;
    CheckBox showMiniPlayerOnMainWindowClose;
    Label libraryPath;
    










    public LibrarySettingsWindow(MainAudioWindow window) {
        this.stage = new Stage();
        stage.setTitle("Music Library Settings");
        this.window=window;

    }

    public void showWindow() {
        VBox preferencePane= new VBox();

        Settings settings= window.getLibrary().getSettings();



        HBox libraryBox= new HBox();
        setLibarary= new Button("Set Library Folder");

        setLibarary.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);


                String path=file.getAbsolutePath();
                settings.setLibraryPath(path);
                libraryPath.setText(" Music Library Folder Path: " + path);



            }
        });
       libraryPath= new Label(" Music Library Folder Path: " + settings.getLibraryPath());
        libraryBox.getChildren().add(setLibarary);
        libraryBox.getChildren().add(libraryPath);
        Label fpCalcPath= new Label("Path To FpCalc: "+settings.getFpCalcPath());


        setFpCalcPath=new Button("Set FpCalc Path");
        setFpCalcPath.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File file = directoryChooser.showDialog(stage);


                settings.setFpCalcPath(file.getAbsolutePath());

            }
        });
        VBox fpCalcBox= new VBox(fpCalcPath, setFpCalcPath);





        reScanFolder= new Button("Rescan Library Folder");
        reScanFolder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                MusicLibrary library=window.getLibrary();
                library.checkForNewSongsinLibraryFolder();
                library.updateSmartPlaylists();
                library.refreshSmartPlaylistPanes();


            }
        });
        libraryBox.getChildren().add(reScanFolder);


       addAllSongsToLibrary = new CheckBox("Add Any Added Songs To Library");
       addAllSongsToLibrary.setSelected(settings.isAddAllAddedSongsToLibrary());
       addAllSongsToLibrary.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setAddAllAddedSongsToLibrary(addAllSongsToLibrary.isSelected());
                moveFilesToLibraryFolder.setDisable(!addAllSongsToLibrary.isSelected());
                copyFilesToLibraryFolder.setDisable(!addAllSongsToLibrary.isSelected());
                getMissingTagsOnAdding.setDisable(!addAllSongsToLibrary.isSelected());



            }
        });

        moveFilesToLibraryFolder = new CheckBox("Move Files to Library Folder on Adding Them To PlayLists");
        moveFilesToLibraryFolder.setSelected(settings.isMoveFilesToLibraryFolder());
        moveFilesToLibraryFolder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setMoveFilesToLibraryFolder(moveFilesToLibraryFolder.isSelected());

                copyFilesToLibraryFolder.setDisable(!(moveFilesToLibraryFolder.isSelected() && moveFilesToLibraryFolder.isVisible()));
                getMissingTagsOnAdding.setDisable(!(moveFilesToLibraryFolder.isSelected() && moveFilesToLibraryFolder.isVisible()));



            }
        });
        moveFilesToLibraryFolder.setDisable(!addAllSongsToLibrary.isSelected());
        copyFilesToLibraryFolder= new CheckBox("Copy Files  Before Moving Them To Library Folder?");
        copyFilesToLibraryFolder.setSelected(settings.isCopyFilesToLibraryFolder());
        copyFilesToLibraryFolder.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setCopyFilesToLibraryFolder(copyFilesToLibraryFolder.isSelected());

            }
        });
        copyFilesToLibraryFolder.setDisable(!moveFilesToLibraryFolder.isSelected());

        getMissingTagsOnAdding= new CheckBox("Get Missing AudioFile Tags Via AcoustID / Music Brainz When Adding Song Files To Library");
        getMissingTagsOnAdding.setSelected(settings.isGetMissingTagsOnAddingSongsToLibrary());
        getMissingTagsOnAdding.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setGetMissingTagsOnAddingSongsToLibrary(getMissingTagsOnAdding.isSelected());

            }
        });
        getMissingTagsOnAdding.setDisable(!moveFilesToLibraryFolder.isSelected());






        organizeLibraryByBitRate= new CheckBox("Organize Library Music Files By Bit Rate");
        organizeLibraryByBitRate.setSelected(settings.isSortLibraryByBitRate());
        organizeLibraryByBitRate.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setSortLibraryByBitRate(organizeLibraryByBitRate.isSelected());


            }
        });

        organizeLibraryByFileType= new CheckBox("Organize Library Music Files By  Audio File Type ");
        organizeLibraryByFileType.setSelected(settings.isSortLibraryByFileType());

        organizeLibraryByFileType.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setSortLibraryByFileType(organizeLibraryByFileType.isSelected());

            }
        });


        saveLibraryOnExit= new CheckBox("Save Music Library on Exiting ");
        saveLibraryOnExit.setSelected(settings.isSaveLibraryOnExit());

        saveLibraryOnExit.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setSaveLibraryOnExit(saveLibraryOnExit.isSelected());

            }
        });


        saveLibraryOnEdits= new CheckBox("Save Music Library On Tag Edits ");
        saveLibraryOnEdits.setSelected(settings.isSaveLibraryOnEdits());

        saveLibraryOnEdits.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setSaveLibraryOnEdits(saveLibraryOnEdits.isSelected());

            }
        });

        showMiniPlayerOnMainWindowClose= new CheckBox("Show Smaller Player on Main Window Close");
        showMiniPlayerOnMainWindowClose.setSelected(settings.isShowMiniPlayerOnMainWindowClose());

        showMiniPlayerOnMainWindowClose.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setShowMiniPlayerOnMainWindowClose(showMiniPlayerOnMainWindowClose.isSelected());

            }
        });
        scanLibraryForNewSongs= new CheckBox("Scan Library Folder For New Songs On Start ");
        scanLibraryForNewSongs.setSelected(settings.isScanLibraryFoldreForNewSongs());

        scanLibraryForNewSongs.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(window.getLibrary().getAllSongs().size()>10000){

                    EventHandler handler= new EventHandler() {
                        @Override
                        public void handle(Event event) {
                            settings.setScanLibraryFoldreForNewSongs(true);

                        }
                    };
                    new OptionPane().showOptionPane("You Have Over 10000 songs in your library !  Rescanning  your library folder will take quite some time and start up will be very slow. Are you sure you wish to enable this option?", "Yes", handler);
                }

                else {

                    settings.setScanLibraryFoldreForNewSongs(scanLibraryForNewSongs.isSelected());

                }
            scanLibraryForNewSongs.setSelected(settings.isScanLibraryFoldreForNewSongs());

            }
        });



        preferencePane.getChildren().add(libraryBox);
        preferencePane.getChildren().add(fpCalcBox);
        preferencePane.getChildren().add(addAllSongsToLibrary);
        preferencePane.getChildren().add(moveFilesToLibraryFolder);
        preferencePane.getChildren().add(copyFilesToLibraryFolder);
        preferencePane.getChildren().add(getMissingTagsOnAdding);
            showNotificationsOnSongChange= new CheckBox("Show Notifications When a New Song Starts Playing");
            showNotificationsOnSongChange.setSelected(settings.isShowNotificationsOnSongChange());

            showNotificationsOnSongChange.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    settings.setShowNotificationsOnSongChange(showNotificationsOnSongChange.isSelected());
                    window.setNotifyOnPlayStart(showNotificationsOnSongChange.isSelected());

                }
            });
            showNotificationsOnProcessComplete= new CheckBox("Show Notifications When Process Completes");
            showNotificationsOnProcessComplete.setSelected(settings.isShowNotifcationOnProcessComplete());

            showNotificationsOnProcessComplete.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    settings.setShowNotifcationOnProcessComplete(showNotificationsOnProcessComplete.isSelected());

                }
            });
            showProcessNotifications= new CheckBox(" Show Process Notifications");
            showProcessNotifications.setSelected(settings.isShowProcessNotifications());

            showProcessNotifications.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    settings.setShowProcessNotifications(showProcessNotifications.isSelected());

                }
            });
            preferencePane.getChildren().add(showNotificationsOnSongChange);
            preferencePane.getChildren().add(showNotificationsOnProcessComplete);
            preferencePane.getChildren().add(showProcessNotifications);



        preferencePane.getChildren().add(saveLibraryOnEdits);
        preferencePane.getChildren().add(saveLibraryOnExit);
        preferencePane.getChildren().add(showMiniPlayerOnMainWindowClose);

        preferencePane.getChildren().add(organizeLibraryByBitRate);
        preferencePane.getChildren().add(organizeLibraryByFileType);
        preferencePane.getChildren().add(scanLibraryForNewSongs);




        stage.setScene(new Scene(preferencePane));
        stage.show();






    }
}
