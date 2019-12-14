package sample.Library;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class SimpleIntegerAdapter extends TypeAdapter<SimpleIntegerProperty> {// gson adapter for javafx simple  string property


    @Override
    public void write(JsonWriter writer, SimpleIntegerProperty simpleIntegerProperty) throws IOException {

        writer.value(simpleIntegerProperty.getValue());// write the simple string string value

    }

    @Override
    public SimpleIntegerProperty read(JsonReader jsonReader) throws IOException {
        return  new SimpleIntegerProperty(jsonReader.nextInt()); // create simple string property from a string
    }
}
