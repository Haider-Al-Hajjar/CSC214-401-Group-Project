package com.example.fantasysortinggame.database;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;

public class FileAdapter extends TypeAdapter<File> {

    @Override
    public void write(JsonWriter out, File value) throws IOException {
        if (value == null) {
            out.nullValue(); // safely write JSON null
            return;
        }
        out.value(value.getPath());
    }


    @Override
    public File read(JsonReader in) throws IOException {
        return new File(in.nextString());
    }
}