package me.jitan.fhpxdemo.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailFragment extends Fragment
{
    private PhotoViewAttacher mAttacher;
    private Drawable mPhoto;
    private String mAuthor;
    @InjectView(R.id.photo_detail_imageview) ImageView mImageView;
    @InjectView(R.id.photo_detail_textview) TextView mTextView;
    @InjectView(R.id.photo_detail_progress_bar) ProgressBar mProgressBar;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        ButterKnife.inject(this, view);

        if (mPhoto != null && mAuthor != null)
        {
            mImageView.setImageDrawable(mPhoto);
            mTextView.setText("Author - " + mAuthor);
            mAttacher = new PhotoViewAttacher(mImageView);
            mAttacher.update();
        }

        return view;
    }

    public void setPhoto(final FhpxPhoto fhpxPhoto)
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mAttacher = new PhotoViewAttacher(mImageView);

        Glide.with(this)
                .load(fhpxPhoto.getUrl())
                .placeholder(fhpxPhoto.getThumb())
                .into(new GlideDrawableImageViewTarget(mImageView)
                {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                            GlideAnimation<? super GlideDrawable> animation)
                    {
                        super.onResourceReady(resource, animation);
                        mPhoto = resource;

                        mAttacher.update();
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mAuthor = fhpxPhoto.getAuthorName();
        mTextView.setText("Author - " + mAuthor);
    }

    public void onEventMainThread(LoadPhotoDetailEvent event)
    {
        setPhoto(event.getFhpxPhoto());
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
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override public void onDestroyView()
    {
        ButterKnife.reset(this);
        super.onDestroyView();
    }
}
