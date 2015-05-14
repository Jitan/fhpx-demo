package me.jitan.fhpxdemo.event;

import java.util.ArrayList;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class AddPhotoSetToGridEvent {
  private final ArrayList<FhpxPhoto> mFhpxPhotoList;

  public AddPhotoSetToGridEvent(ArrayList<FhpxPhoto> fhpxPhotoList) {
    mFhpxPhotoList = fhpxPhotoList;
  }

  public ArrayList<FhpxPhoto> getFhpxPhotoList() {
    return mFhpxPhotoList;
  }
}
