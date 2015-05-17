package me.jitan.fhpxdemo.data;

import java.util.List;
import java.util.Map;
import me.jitan.fhpxdemo.data.model.SearchResult;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface FhpxSearchService {
  @GET("/photos/search") void searchPhotos(@QueryMap Map<String, String> searchOptions,
      @Query(value = "image_size[]", encodeValue = false) List<PhotoSize> imageSizes, Callback<SearchResult> cb);
}