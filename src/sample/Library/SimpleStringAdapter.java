package sample.Library;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class SimpleStringAdapter extends TypeAdapter<SimpleStringProperty> {// gson adapter for javafx simple  string property


    @Override
    public void write(JsonWriter writer, SimpleStringProperty simpleStringProperty) throws IOException {

        writer.value(simpleStringProperty.getValue());// write the simple string string value

    }

    @Override
    public SimpleStringProperty read(JsonReader jsonReader) throws IOException {
        return  new SimpleStringProperty(jsonReader.nextString()); // create simple string property from a string
    }
}
