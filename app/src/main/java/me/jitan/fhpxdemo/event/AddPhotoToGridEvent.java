package me.jitan.fhpxdemo.event;

import me.jitan.fhpxdemo.data.model.Photo;

public class AddPhotoToGridEvent {
  private final Photo mPhoto;

  public AddPhotoToGridEvent(Photo photo) {
    mPhoto = photo;
  }

  public Photo getPhoto() {
    return mPhoto;
  }
}
