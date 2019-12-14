package sample.Utilities;


import javafx.scene.control.TextField;

public class NumberField extends TextField {

    public NumberField() {
    }


    public int getIntValue(){


        return Integer.valueOf(getText());


    }


}
