package me.jitan.fhpxdemo;

public class FhpxImage
{
    private final String thumbUrl, url, authorName;

    public FhpxImage(String thumbUrl, String url, String authorName)
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
