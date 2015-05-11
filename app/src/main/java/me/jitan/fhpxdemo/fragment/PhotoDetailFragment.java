package me.jitan.fhpxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;

import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.model.FhpxPhoto;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailFragment extends Fragment
{
    private ImageView mImageView;
    private TextView mTextView;
    private PhotoViewAttacher mAttacher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        mTextView = (TextView) view.findViewById(R.id.photo_detail_textview);
        mImageView = (ImageView) view.findViewById(R.id.photo_detail_imageview);
        mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        mAttacher = new PhotoViewAttacher(mImageView);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);

        return view;
    }

    public void setPhoto(FhpxPhoto fhpxPhoto)
    {
        Ion.with(mImageView)
                .deepZoom()
                .load(fhpxPhoto.getLargeUrl());

        mTextView.setText("Author - " + fhpxPhoto.getAuthorName());
        mAttacher.update();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mAttacher.cleanup();
    }
}
