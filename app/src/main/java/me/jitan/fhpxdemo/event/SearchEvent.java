package me.jitan.fhpxdemo.event;

public class SearchEvent {
  private final String mSearchQuery, mSortOption;

  public SearchEvent(String searchQuery, String sortOption) {
    mSearchQuery = searchQuery;
    mSortOption = sortOption;
  }

  public String getSearchQuery() {
    return mSearchQuery;
  }

  public String getSortOption() {
    return mSortOption;
  }

}