package me.jitan.fhpxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailFragment extends Fragment
{
    private ImageView mImageView;
    private TextView mTextView;
    private PhotoViewAttacher mAttacher;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onStop()
    {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);

        mTextView = (TextView) view.findViewById(R.id.photo_detail_textview);
        mImageView = (ImageView) view.findViewById(R.id.photo_detail_imageview);

        return view;
    }

    public void setPhoto(FhpxPhoto fhpxPhoto)
    {
        Ion.with(mImageView)
                .load(fhpxPhoto.getUrl())
                .setCallback(new FutureCallback<ImageView>()
                {
                    @Override
                    public void onCompleted(Exception e, ImageView result)
                    {
                        mAttacher = new PhotoViewAttacher(mImageView);
                        mAttacher.update();
                    }
                });
        mTextView.setText("Author - " + fhpxPhoto.getAuthorName());
    }

    public void onEventMainThread(LoadPhotoDetailEvent event)
    {
        setPhoto(event.getFhpxPhoto());
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mAttacher.cleanup();
    }
}
