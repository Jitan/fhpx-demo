package me.jitan.fhpxdemo.event;

public class SearchEvent
{
    private final String mSearchQuery, mSortOptions;

    public SearchEvent(String searchQuery, String sortOptions)
    {
        this.mSearchQuery = searchQuery;
        this.mSortOptions = sortOptions;
    }

    public String getSearchQuery()
    {
        return mSearchQuery;
    }

    public String getSortOptions()
    {
        return mSortOptions;
    }
}