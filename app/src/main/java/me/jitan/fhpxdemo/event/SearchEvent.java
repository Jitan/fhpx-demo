package me.jitan.fhpxdemo.event;

public class SearchEvent
{
    private final String searchQuery;

    public SearchEvent(String searchQuery)
    {
        this.searchQuery = searchQuery;
    }

    public String getSearchQuery()
    {
        return searchQuery;
    }
}
