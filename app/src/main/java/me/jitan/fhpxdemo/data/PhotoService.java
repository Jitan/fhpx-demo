package me.jitan.fhpxdemo.data;

import android.util.Log;
import de.greenrobot.event.EventBus;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.jitan.fhpxdemo.data.model.Photo;
import me.jitan.fhpxdemo.data.model.SearchResults;
import me.jitan.fhpxdemo.event.AddPhotoListToGridEvent;
import me.jitan.fhpxdemo.event.CouldNotLoadImageEvent;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PhotoService {
  private final static String Fhpx_Api_Endpoint = "https://api.500px.com/v1";
  private final static String Fhpx_Consumer_Key = "dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2";
  private final static List<PhotoSize> Image_Sizes =
      Arrays.asList(PhotoSize.THUMB, PhotoSize.NORMAL, PhotoSize.LARGE);
  private final static String Number_Of_Search_Results = "100";

  private static PhotoService mInstance;
  private static FhpxSearchService mFhpxSearchService;

  private Map<String, String> mQueryParams;
  private String mLastSearchQuery, mLastSortOption;
  private int mLastPageLoaded;

  public static PhotoService getInstance() {
    if (mInstance == null) {
      mInstance = new PhotoService();
    }
    return mInstance;
  }

  private PhotoService() {
    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Fhpx_Api_Endpoint)
        .setLogLevel(RestAdapter.LogLevel.BASIC)
        .build();

    mFhpxSearchService = restAdapter.create(FhpxSearchService.class);
    mQueryParams = new HashMap<>();
    mQueryParams.put("rpp", Number_Of_Search_Results);
    mQueryParams.put("consumer_key", Fhpx_Consumer_Key);
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
    mFhpxSearchService.searchPhotos(mQueryParams, Image_Sizes, new SearchResultCallback());
  }

  private void updateHistory(String searchQuery, String sortOption, int pageNumber) {
    mLastSearchQuery = searchQuery;
    mLastSortOption = sortOption;
    mLastPageLoaded = pageNumber;
  }

  private void updateQueryParams(String searchQuery, String sortOption, int pageNumber) {
    mQueryParams.put("term", searchQuery);
    mQueryParams.put("sort", sortOption);
    mQueryParams.put("page", String.valueOf(pageNumber));
  }

  private String cleanSearchQuery(String searchQuery) {
    searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]+", "+").replaceAll("\\+$", "");
    return searchQuery;
  }

  private class SearchResultCallback implements Callback<SearchResults> {
    @Override public void success(SearchResults searchResults, Response response) {
      EventBus.getDefault().post(new AddPhotoListToGridEvent(searchResults.getPhotos()));
    }

    @Override public void failure(RetrofitError error) {
      EventBus.getDefault().post(new CouldNotLoadImageEvent());
    }
  }

  public static String getPhotoUrl(Photo photo, PhotoSize size) {
    switch (size) {
      case THUMB:
        return photo.getImages().get(0).getUrl();
      case NORMAL:
        return photo.getImages().get(1).getUrl();
      case LARGE:
        return photo.getImages().get(2).getUrl();
      default:
        throw new InvalidPhotoSizeException();
    }
  }
}
