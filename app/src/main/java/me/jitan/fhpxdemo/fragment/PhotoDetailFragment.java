package me.jitan.fhpxdemo.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private FhpxPhoto mLastFhpxPhoto;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (mLastFhpxPhoto != null)
        {
            setPhoto(mLastFhpxPhoto);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        EventBus.getDefault().register(this);
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

        mImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setPhoto(FhpxPhoto fhpxPhoto)
    {
        mLastFhpxPhoto = fhpxPhoto;

        Ion.with(mImageView)
                .load(fhpxPhoto.getUrl())
                .setCallback(new FutureCallback<ImageView>()
                {
                    @Override
                    public void onCompleted(Exception e, ImageView result)
                    {
                        mAttacher = new PhotoViewAttacher(mImageView);
                    }
                });

        mTextView.setText("Author - " + fhpxPhoto.getAuthorName());
        mAttacher.update();
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
