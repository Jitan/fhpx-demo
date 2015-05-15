package me.jitan.fhpxdemo.event;

import java.util.List;
import me.jitan.fhpxdemo.data.model.Photo;

public class AddPhotoListToGridEvent {
  private final List<Photo> mPhotoList;

  public AddPhotoListToGridEvent(List<Photo> photoList) {
    mPhotoList = photoList;
  }

  public List<Photo> getPhotoList() {
    return mPhotoList;
  }
}
