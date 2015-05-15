package me.jitan.fhpxdemo.retrofit;

import java.util.List;
import java.util.Map;
import me.jitan.fhpxdemo.retrofit.json.SearchResult;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface FhpxSearch {
  @GET("/photos/search") void searchPhotos(@QueryMap Map<String, String> searchOptions,
      @Query(value = "image_size[]", encodeValue = false) List<String> imageSizes, Callback<SearchResult> cb);
}