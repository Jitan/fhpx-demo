package me.jitan.fhpxdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class IonClient {
  private final static String PhotoApi_BaseUrl = "https://api.500px.com/v1/photos/search";
  private final static String Fhpx_ConsumerKey = "dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2";

  private final static String Thumb_Size = "3";
  private final static String Normal_Size = "1080";
  private final static String Large_Size = "2048";
  private final static String Number_Of_Search_Results = "100";

  private final Context mContext;
  private static IonClient mInstance;

  private String mLastSearchQuery, mLastSortOption;
  private int mLastPageLoaded;

  private IonClient(Context context) {
    mContext = context.getApplicationContext();
  }

  public static IonClient getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new IonClient(context);
    }
    return mInstance;
  }

  public void loadNextPage() {
    if (mLastSearchQuery != null) {
      doSearch(mLastSearchQuery, mLastSortOption, mLastPageLoaded + 1);
    }
  }

  public void doSearch(String searchQuery, String sortOption) {
    doSearch(searchQuery, sortOption, 1);
  }

  private void doSearch(String searchQuery, String sortOption, int pageNumber) {
    Log.d("json-parse", "Starting JSON download for page " + pageNumber);
    searchQuery = cleanSearchQuery(searchQuery);
    mLastSearchQuery = searchQuery;
    mLastSortOption = sortOption;
    mLastPageLoaded = pageNumber;

    if (pageNumber == 1) {
      Toast.makeText(mContext, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(mContext, "Loading more results..", Toast.LENGTH_SHORT).show();
    }

    // rating — Sort by current rating. (default)
    // highest_rating — Sort by highest rating achieved (Menu action Sort by rating)
    sortOption = (sortOption == null) ? "rating" : sortOption;

    //OkHttpClient client = new OkHttpClient();
    //
    //Request request = new Request.Builder().url(PhotoApi_BaseUrl +
    //    "?term=" + searchQuery +
    //    "&sort=" + sortOption +
    //    "&page=" + pageNumber +
    //    "&image_size[]=" + Thumb_Size +
    //    "&image_size[]=" + Normal_Size +
    //    "&image_size[]=" + Large_Size +
    //    "&rpp=" + Number_Of_Search_Results +
    //    "&consumer_key=" + Fhpx_ConsumerKey).build();
    //
    //client.newCall(request).enqueue(new Callback() {
    //  @Override public void onFailure(Request request, IOException e) {
    //    Toast.makeText(mContext, "Connection error", Toast.LENGTH_SHORT).show();
    //  }
    //
    //  @Override public void onResponse(Response response) {
    //    if (!response.isSuccessful()) {
    //      Toast.makeText(mContext, "Connection error", Toast.LENGTH_SHORT).show();
    //    }
    //
    //    JsonObject jsonResponse = null;
    //    try {
    //      jsonResponse = new JsonParser().parse(response.body().string()).getAsJsonObject();
    //    } catch (IOException e) {
    //      e.printStackTrace();
    //    }
    //
    //    Log.d("json-parse", "Finished JSON download");
    //    new JsonToFphxImageTask().execute(jsonResponse);
    //  }
    //});

    Ion.with(mContext)
        .load(PhotoApi_BaseUrl +
            "?term=" + searchQuery +
            "&sort=" + sortOption +
            "&page=" + pageNumber +
            "&image_size[]=" + Thumb_Size +
            "&image_size[]=" + Normal_Size +
            "&image_size[]=" + Large_Size +
            "&rpp=" + Number_Of_Search_Results +
            "&consumer_key=" + Fhpx_ConsumerKey)
        .asJsonObject()
        .setCallback(new FutureCallback<JsonObject>() {
          @Override public void onCompleted(Exception e, JsonObject result) {
            Log.d("json-parse", "Finished JSON download");

            if (e == null) {
              new JsonToFphxImageTask().execute(result);
            } else {
              Toast.makeText(mContext, "Connection error", Toast.LENGTH_SHORT).show();
            }
          }
        });
  }

  private String cleanSearchQuery(String searchQuery) {
    searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]+", "+").replaceAll("\\+$", "");
    return searchQuery;
  }

  private static final class JsonToFphxImageTask
      extends AsyncTask<JsonObject, Void, ArrayList<FhpxPhoto>> {
    @Override protected ArrayList<FhpxPhoto> doInBackground(JsonObject... params) {
      Log.d("json-parse", "Starting JSON parsing");
      ArrayList<FhpxPhoto> fhpxPhotoSet = new ArrayList<>();

      String authorName = "", thumbUrl = "", url = "", largeUrl = "";
      JsonObject userObject, photoObject;
      JsonArray photos = params[0].getAsJsonArray("photos");

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
            case Thumb_Size:
              thumbUrl = imageUrlObject.get("url").getAsString();
              break;
            case Normal_Size:
              url = imageUrlObject.get("url").getAsString();
              break;
            case Large_Size:
              largeUrl = imageUrlObject.get("url").getAsString();
              break;
          }
        }

        fhpxPhotoSet.add(new FhpxPhoto(thumbUrl, url, largeUrl, authorName));
      }
      Log.d("json-parse", "Finished JSON parsing");
      return fhpxPhotoSet;
    }

    @Override protected void onPostExecute(ArrayList<FhpxPhoto> fhpxPhotoSet) {
      EventBus.getDefault().post(new AddPhotoSetToGridEvent(fhpxPhotoSet));
    }
  }
}
