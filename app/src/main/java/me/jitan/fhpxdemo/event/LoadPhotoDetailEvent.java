package me.jitan.fhpxdemo.event;

import android.graphics.drawable.Drawable;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class LoadPhotoDetailEvent {
  private final FhpxPhoto mFhpxPhoto;
  private final Drawable mThumb;

  public LoadPhotoDetailEvent(FhpxPhoto fhpxPhoto, Drawable thumb) {
    mFhpxPhoto = fhpxPhoto;
    mThumb = thumb;
  }

  public FhpxPhoto getFhpxPhoto() {
    return mFhpxPhoto;
  }

  public Drawable getThumb() {
    return mThumb;
  }
}
