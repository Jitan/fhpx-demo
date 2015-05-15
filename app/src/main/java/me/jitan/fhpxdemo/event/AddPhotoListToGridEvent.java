package me.jitan.fhpxdemo.event;

import java.util.List;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class AddPhotoListToGridEvent {
  private final List<FhpxPhoto> mFhpxPhotoList;

  public AddPhotoListToGridEvent(List<FhpxPhoto> fhpxPhotoList) {
    mFhpxPhotoList = fhpxPhotoList;
  }

  public List<FhpxPhoto> getFhpxPhotoList() {
    return mFhpxPhotoList;
  }
}
