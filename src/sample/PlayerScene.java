package sample;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import sample.MusicPlayers.PlayerState;
import sample.Windows.PlaylistCreatorWindow;

import java.util.HashMap;

public class PlayerScene extends Scene {
    private HashMap<KeyCode, SimpleBooleanProperty> keysPressed= new HashMap<>(); // hashmap for keys pressed
   private  PlayerWindow window;
   private BooleanBinding volumeUP;
   private BooleanBinding volumeDOWN;
    private BooleanBinding panLEFT;
    private BooleanBinding panRIGHT;
    private BooleanBinding panCENTER;
    private BooleanBinding playNext;
    private BooleanBinding newEmptyPlayList;
    private BooleanBinding newPlayList;
    private BooleanBinding fastFoward;
    private BooleanBinding rewind;
    private BooleanBinding restart;
    private BooleanBinding reloadPlayListTabs;
    private BooleanBinding editTracks;
    private BooleanBinding lockTrackSorting;
    private  int  counter=0;
    public PlayerScene(PlayerWindow window, Parent root) {
        super(root);
        this.window=window;
        KeyCode[]  keys=KeyCode.values(); // make the map of keyboard keys to boolean values
        int size= keys.length;
        for(int count=0; count<size; count++){
            keysPressed.put(keys[count], new SimpleBooleanProperty(false));
        }
     setOnKeyPressed(new EventHandler<KeyEvent>() { // set key boolean to true if pressed
         @Override
         public void handle(KeyEvent event) {
        keysPressed.get(event.getCode()).setValue(true);
         }
        });
        setOnKeyReleased(new EventHandler<KeyEvent>() { // set key to false once released
            @Override
            public void handle(KeyEvent event) {
                keysPressed.get(event.
                        getCode()).setValue(false);

            }
        });
        volumeUP = keysPressed.get(KeyCode.COMMAND).and(keysPressed.get(KeyCode.UP));
        volumeUP.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue==true) {
                        window.increaseVolume();
                    }

            }
        });
        volumeDOWN = keysPressed.get(KeyCode.COMMAND).and(keysPressed.get(KeyCode.DOWN));
        volumeDOWN.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.decreaseVolume();
                }

                           }
        });
        panLEFT = keysPressed.get(KeyCode.COMMAND).and(keysPressed.get(KeyCode.LEFT));
        panLEFT.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.panLeft();
                }

                           }
        });
        panRIGHT = keysPressed.get(KeyCode.COMMAND).and(keysPressed.get(KeyCode.RIGHT));
        panRIGHT.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.panRight();
                }

                           }
        });
        panCENTER = keysPressed.get(KeyCode.COMMAND).and(keysPressed.get(KeyCode.C));
        panCENTER.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.panCenter();
                }
            }
        });
        keysPressed.get(KeyCode.PLUS).addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(window.isMuted()){
                    window.unMute();
                }
                else {
                    window.mute();
                }
            }
        });
        keysPressed.get(KeyCode.SPACE).addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(window.getPlayerState()== PlayerState.PLAYING){
                    window.pause();
                }
                else {
                    window.play();
                }
            }
        });
        keysPressed.get(KeyCode.ESCAPE).addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(window.getPlayerState()== PlayerState.PLAYING){
                    window.pause();
                }
                else {
                    window.play();
                }
            }
        });
        playNext = keysPressed.get(KeyCode.ALT).and(keysPressed.get(KeyCode.RIGHT));
        playNext.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.playNext();
                }

            }
        });


        fastFoward = keysPressed.get(KeyCode.SHIFT).and(keysPressed.get(KeyCode.RIGHT));
        fastFoward.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.fastForward();
                }

            }
        });
        rewind = keysPressed.get(KeyCode.SHIFT).and(keysPressed.get(KeyCode.LEFT));
        rewind.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.rewind();
                }

            }
        });
        restart= keysPressed.get(KeyCode.ALT).and(keysPressed.get(KeyCode.LEFT));
        restart.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.seekPlay(0);
                }

                            }
        });

        reloadPlayListTabs= keysPressed.get(KeyCode.ALT).and(keysPressed.get(KeyCode.LEFT));
        reloadPlayListTabs.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true) {

                    window.reloadPlaylistTabs();
                }

            }
        });

    }
}
