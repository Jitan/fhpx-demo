package me.jitan.fhpxdemo.event;

public class SearchEvent
{
    private final String mSearchQuery, mSortOption;

    public SearchEvent(String searchQuery, String sortOption)
    {
        this.mSearchQuery = searchQuery;
        this.mSortOption = sortOption;
    }

    public String getSearchQuery()
    {
        return mSearchQuery;
    }

    public String getSortOption()
    {
        return mSortOption;
    }
}