package me.jitan.fhpxdemo.event;

import me.jitan.fhpxdemo.model.FhpxPhoto;

public class LoadPhotoDetailEvent {
  private final FhpxPhoto mFhpxPhoto;

  public LoadPhotoDetailEvent(FhpxPhoto fhpxPhoto) {
    mFhpxPhoto = fhpxPhoto;
  }

  public FhpxPhoto getFhpxPhoto() {
    return mFhpxPhoto;
  }
}
