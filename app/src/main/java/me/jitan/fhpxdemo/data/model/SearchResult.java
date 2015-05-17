package me.jitan.fhpxdemo.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class SearchResult {

  @SerializedName("current_page") @Expose private Integer currentPage;
  @SerializedName("total_pages") @Expose private Integer totalPages;
  @SerializedName("total_items") @Expose private Integer totalItems;
  @Expose private List<Photo> photos = new ArrayList<Photo>();

  /**
   * @return The currentPage
   */
  public Integer getCurrentPage() {
    return currentPage;
  }

  /**
   * @param currentPage The current_page
   */
  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  /**
   * @return The totalPages
   */
  public Integer getTotalPages() {
    return totalPages;
  }

  /**
   * @param totalPages The total_pages
   */
  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  /**
   * @return The totalItems
   */
  public Integer getTotalItems() {
    return totalItems;
  }

  /**
   * @param totalItems The total_items
   */
  public void setTotalItems(Integer totalItems) {
    this.totalItems = totalItems;
  }

  /**
   * @return The photos
   */
  public List<Photo> getPhotos() {
    return photos;
  }

  /**
   * @param photos The photos
   */
  public void setPhotos(List<Photo> photos) {
    this.photos = photos;
  }
}
