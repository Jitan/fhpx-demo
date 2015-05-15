package me.jitan.fhpxdemo.retrofit;

import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class FhpxPhotoAdapter implements JsonDeserializer<List<FhpxPhoto>> {

  @Override public List<FhpxPhoto> deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {
    final JsonObject searchResultJson = json.getAsJsonObject();

    Log.d("json-parse", "Starting JSON parsing");
    ArrayList<FhpxPhoto> fhpxPhotoSet = new ArrayList<>();

    String authorName = "", thumbUrl = "", url = "", largeUrl = "";
    JsonObject userObject, photoObject;
    JsonArray photos = searchResultJson.getAsJsonArray("photos");

    for (JsonElement photo : photos) {
      photoObject = photo.getAsJsonObject();
      userObject = photoObject.get("user").getAsJsonObject();

      if (!userObject.get("firstname").isJsonNull()) {
        authorName = userObject.get("firstname").getAsString();
      }

      if (!userObject.get("lastname").isJsonNull()) {
        authorName += " " + userObject.get("lastname").getAsString();
      }

      JsonArray imageUrls = photoObject.get("images").getAsJsonArray();
      for (JsonElement imageUrl : imageUrls) {
        JsonObject imageUrlObject = imageUrl.getAsJsonObject();
        String size = imageUrlObject.get("size").getAsString();

        switch (size) {
          case FhpxPhotoService.Thumb_Size:
            thumbUrl = imageUrlObject.get("url").getAsString();
            break;
          case FhpxPhotoService.Normal_Size:
            url = imageUrlObject.get("url").getAsString();
            break;
          case FhpxPhotoService.Large_Size:
            largeUrl = imageUrlObject.get("url").getAsString();
            break;
        }
      }

      fhpxPhotoSet.add(new FhpxPhoto(thumbUrl, url, largeUrl, authorName));
    }
    Log.d("json-parse", "Finished JSON parsing");
    return fhpxPhotoSet;
  }
}
