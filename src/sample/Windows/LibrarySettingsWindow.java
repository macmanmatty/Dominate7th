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
        import tray.notification.NotificationType;
        import java.io.File;
        import java.util.concurrent.CountDownLatch;

public class LibrarySettingsWindow {
    private    Stage stage;
    private MainAudioWindow window;
    private Button setLibarary;
    private Button reScanFolder;
    private Button setFpCalcPath;
    private Button removeSongsWithOutFile;
    private CheckBox moveFilesToLibraryFolder;
    private CheckBox getMissingTagsOnAdding;
    private CheckBox organizeLibraryByFileType;
    private CheckBox organizeLibraryByBitRate;
    private CheckBox scanLibraryForNewSongs;
    private CheckBox showNotificationsOnSongChange;
    private CheckBox showNotificationsOnProcessComplete;
    private CheckBox showProcessNotifications;
    private CheckBox saveLibraryOnExit;
    private CheckBox saveLibraryOnEdits;
    private CheckBox copyFilesToLibraryFolder;
    private CheckBox addAllSongsToLibrary;
    private CheckBox showMiniPlayerOnMainWindowClose;
    private CheckBox displayProgressWindowsForAddingSongs;
    private CheckBox removeSongsWithMissingFiles;
    private CheckBox askToFindMissingFilesForSongs;
    private  Label libraryPath;

    public LibrarySettingsWindow(MainAudioWindow window) {
        this.stage = new Stage();
        stage.setTitle("Music Library Settings");
        this.window=window;
    }
    public void showWindow() {
        VBox preferencePane= new VBox();
        Settings settings= window.getLibrary().getSettings();
        VBox libraryBox= new VBox();
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
        removeSongsWithOutFile= new Button("Clean Library");
        removeSongsWithOutFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cleanLibrary();
            }
        });
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
        libraryPath= new Label(" Music Library Folder Path: " + settings.getLibraryPath());
        HBox path =new HBox(setLibarary, libraryPath);
        path.setSpacing(5);
        libraryBox.getChildren().add(path);
        Label fpCalcPath= new Label("Path To FpCalc: "+settings.getFpCalcPath());
        HBox buttons= new HBox(reScanFolder, removeSongsWithOutFile);
        buttons.setSpacing(5);
        libraryBox.getChildren().add(buttons);
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

        askToFindMissingFilesForSongs = new CheckBox("Ask To Find Missing Files For Songs");
        askToFindMissingFilesForSongs.setSelected(settings.isAskToFindMissingFilesForSongs());
        askToFindMissingFilesForSongs.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setAskToFindMissingFilesForSongs(askToFindMissingFilesForSongs.isSelected());
                removeSongsWithMissingFiles.setDisable(askToFindMissingFilesForSongs.isSelected());
                window.setFindFile(askToFindMissingFilesForSongs.isSelected());


            }
        });


        removeSongsWithMissingFiles = new CheckBox("Remove Songs With Missing Files  on Discovery" );
        removeSongsWithMissingFiles.setSelected(settings.isRemoveSongsWithMissingFilesOnDiscovery());
        removeSongsWithMissingFiles.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setRemoveSongsWithMissingFilesOnDiscovery(removeSongsWithMissingFiles.isSelected());


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
        displayProgressWindowsForAddingSongs = new CheckBox("Display A Progress When Window  Adding Songs To Playlists ");
        displayProgressWindowsForAddingSongs.setSelected(settings.isDisplayProgressWindowsForAddingSongs());
        displayProgressWindowsForAddingSongs.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                settings.setDisplayProgressWindowsForAddingSongs(displayProgressWindowsForAddingSongs.isSelected());
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
        preferencePane.getChildren().add(removeSongsWithMissingFiles);
        preferencePane.getChildren().add(askToFindMissingFilesForSongs);
        preferencePane.getChildren().add(displayProgressWindowsForAddingSongs);
        preferencePane.setSpacing(5);
        stage.setScene(new Scene(preferencePane));
        stage.show();
    }
    public void cleanLibrary(){
        CountDownLatch countDownLatch= new CountDownLatch(1);
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                Runnable runnable= new Runnable() {
                    @Override
                    public void run() {
                        MusicLibrary library=window.getLibrary();
                        library.removeNonExistingSongsFromLibrary();
                        library.removeNonExistingSongsFromPlaylists();
                        countDownLatch.countDown();
                        if(showProcessNotifications.isSelected()==true){
                            Notifications notifications= new Notifications();
                            notifications.showAudioProcessNotification("Cleaned" , "Cleaning Of Library Completed ", NotificationType.SUCCESS);
                        }
                    }
                };
                Thread thread= new Thread(runnable);
                thread.start();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                }
                window.saveLibrary();
            }
        };
    }
}
