package me.jitan.fhpxdemo.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class ImageAdapter extends ArrayAdapter<FhpxPhoto> {
  private LayoutInflater mInflater;
  private Fragment mFragment;
  private ViewHolder mHolder;

  public ImageAdapter(Fragment fragment) {
    super(fragment.getActivity(), 0);
    mInflater = LayoutInflater.from((fragment.getActivity()));
    mFragment = fragment;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView != null) {
      mHolder = (ViewHolder) convertView.getTag();
    } else {
      convertView = mInflater.inflate(R.layout.grid_item, parent, false);
      mHolder = new ViewHolder(convertView);
      convertView.setTag(mHolder);
    }
    loadThumb(position);
    return convertView;
  }

  private void loadThumb(int position) {
    final FhpxPhoto fhpxPhoto = getItem(position);

    Glide.with(mFragment)
        .load(fhpxPhoto.getThumbUrl())
        .crossFade()
        .into(new GlideDrawableImageViewTarget(mHolder.imageView) {
          @Override public void onResourceReady(GlideDrawable resource,
              GlideAnimation<? super GlideDrawable> animation) {
            super.onResourceReady(resource, animation);
            fhpxPhoto.setThumb(resource);
          }
        });
  }

  static class ViewHolder {
    @InjectView(R.id.grid_item_image) ImageView imageView;

    public ViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}