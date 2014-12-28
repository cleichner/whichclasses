package com.whichclasses.gson;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.whichclasses.model.Department;

public class DepartmentTypeAdapter extends TypeAdapter<Department> {

  public static TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      return (Department.class.isAssignableFrom(type.getRawType())
          ? (TypeAdapter<T>) new DepartmentTypeAdapter() : null);
    }
  };
  
  @Override
  public Department read(JsonReader arg0) throws IOException {
    throw new UnsupportedOperationException("Can't read from Json");
  }

  @Override
  public void write(JsonWriter writer, Department department) throws IOException {
    writer.beginObject();
    writer.name("identifier").value(department.getShortName());
    writer.name("name").value(department.getFullName());
    writer.endObject();
  }

}
