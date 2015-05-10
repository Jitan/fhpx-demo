package me.jitan.fhpxdemo.model;

public class FhpxPhoto
{
    private final String thumbUrl, url, authorName;

    public FhpxPhoto(String thumbUrl, String url, String authorName)
    {
        this.thumbUrl = thumbUrl;
        this.url = url;
        this.authorName = authorName;
    }

    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public String getUrl()
    {
        return url;
    }

    public String getAuthorName()
    {
        return authorName;
    }
}
