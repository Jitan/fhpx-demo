package me.jitan.fhpxdemo;

import java.util.List;
import java.util.Map;
import me.jitan.fhpxdemo.model.FhpxPhoto;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.http.QueryMap;

public interface FhpxSearch {
  @GET("/photos/search") void searchPhotos(@QueryMap Map<String, String> searchOptions,
      @Query(value = "image_size[]", encodeValue = false) List<String> imageSizes, Callback<List<FhpxPhoto>> cb);
}