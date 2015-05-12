package me.jitan.fhpxdemo.model;

import android.graphics.drawable.Drawable;

public class FhpxPhoto
{
    private final String thumbUrl, url, largeUrl, authorName;
    private Drawable thumb;

    public FhpxPhoto(String thumbUrl, String url, String largeUrl, String authorName)
    {
        this.thumbUrl = thumbUrl;
        this.url = url;
        this.largeUrl = largeUrl;
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

    public String getLargeUrl()
    {
        return largeUrl;
    }

    public Drawable getThumb()
    {
        return thumb;
    }

    public void setThumb(Drawable thumb)
    {
        this.thumb = thumb;
    }
}
