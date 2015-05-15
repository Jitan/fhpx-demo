package me.jitan.fhpxdemo.event;

import android.graphics.drawable.Drawable;
import me.jitan.fhpxdemo.data.model.Photo;

public class LoadPhotoDetailEvent {
  private final Photo mPhoto;
  private final Drawable mThumb;

  public LoadPhotoDetailEvent(Photo photo, Drawable thumb) {
    mPhoto = photo;
    mThumb = thumb;
  }

  public Photo getPhoto() {
    return mPhoto;
  }

  public Drawable getThumb() {
    return mThumb;
  }
}
