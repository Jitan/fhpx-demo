package me.jitan.fhpxdemo.retrofit;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.greenrobot.event.EventBus;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class FhpxPhotoService {
  private final static String Fhpx_Api_Endpoint = "https://api.500px.com/v1";
  private final static String Fhpx_Consumer_Key = "dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2";
  public final static String Thumb_Size = "3";
  public final static String Normal_Size = "1080";
  public final static String Large_Size = "2048";
  private final static List<String> Image_Sizes =
      Arrays.asList(Thumb_Size, Normal_Size, Large_Size);
  private final static String Number_Of_Search_Results = "100";

  private final Context mContext;
  private static FhpxPhotoService mInstance;
  private static FhpxSearch mFhpxSearch;

  private Map<String, String> mApiQueryOptions;
  private String mLastSearchQuery, mLastSortOption;
  private int mLastPageLoaded;

  public static FhpxPhotoService getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new FhpxPhotoService(context);
    }
    return mInstance;
  }

  private FhpxPhotoService(Context context) {

    mContext = context.getApplicationContext();

    Type fhpxType = new TypeToken<List<FhpxPhoto>>(){}.getType();
    Gson gson =
        new GsonBuilder().registerTypeAdapter(fhpxType, new FhpxPhotoAdapter()).create();

    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Fhpx_Api_Endpoint)
        .setLogLevel(RestAdapter.LogLevel.BASIC)
        .setConverter(new GsonConverter(gson))
        .build();

    mFhpxSearch = restAdapter.create(FhpxSearch.class);

    mApiQueryOptions = new HashMap<>();
    mApiQueryOptions.put("rpp", Number_Of_Search_Results);
    mApiQueryOptions.put("consumer_key", Fhpx_Consumer_Key);
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

    // rating — Sort by current rating. (default)
    // highest_rating — Sort by highest rating achieved (Menu action Sort by rating)
    sortOption = (sortOption == null) ? "rating" : sortOption;

    updateHistory(searchQuery, sortOption, pageNumber);
    updateQueryParams(searchQuery, sortOption, pageNumber);
    makeInfoToast(searchQuery, pageNumber);

    mFhpxSearch.searchPhotos(mApiQueryOptions, Image_Sizes, new FhpxPhotoCallBack());
  }

  private void updateQueryParams(String searchQuery, String sortOption, int pageNumber) {
    mApiQueryOptions.put("term", searchQuery);
    mApiQueryOptions.put("sort", sortOption);
    mApiQueryOptions.put("page", String.valueOf(pageNumber));
  }

  private void updateHistory(String searchQuery, String sortOption, int pageNumber) {
    mLastSearchQuery = searchQuery;
    mLastSortOption = sortOption;
    mLastPageLoaded = pageNumber;
  }

  private void makeInfoToast(String searchQuery, int pageNumber) {
    if (pageNumber == 1) {
      Toast.makeText(mContext, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(mContext, "Loading more results..", Toast.LENGTH_SHORT).show();
    }
  }

  private String cleanSearchQuery(String searchQuery) {
    searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]+", "+").replaceAll("\\+$", "");
    return searchQuery;
  }

  private class FhpxPhotoCallBack implements Callback<List<FhpxPhoto>> {
    @Override public void success(List<FhpxPhoto> fhpxPhotos, Response response) {
      EventBus.getDefault().post(new AddPhotoSetToGridEvent(fhpxPhotos));
    }

    @Override public void failure(RetrofitError error) {
      Toast.makeText(mContext, "Connection error", Toast.LENGTH_SHORT).show();
    }
  }
}
