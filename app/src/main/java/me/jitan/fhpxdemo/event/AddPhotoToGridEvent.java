package me.jitan.fhpxdemo.event;

import me.jitan.fhpxdemo.model.FhpxPhoto;

public class AddPhotoToGridEvent
{
    private final FhpxPhoto mFhpxPhoto;

    public AddPhotoToGridEvent(FhpxPhoto fhpxPhoto)
    {
        mFhpxPhoto = fhpxPhoto;
    }

    public FhpxPhoto getFhpxPhoto()
    {
        return mFhpxPhoto;
    }
}
