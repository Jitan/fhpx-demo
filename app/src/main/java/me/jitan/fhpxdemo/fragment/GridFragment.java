package me.jitan.fhpxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.adapter.ImageAdapter;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.event.AddPhotoToGridEvent;
import me.jitan.fhpxdemo.event.LoadNextPageEvent;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;

public class GridFragment extends Fragment {
  private static int Photo_Scroll_Buffer = 50;
  private ImageAdapter mImageAdapter;
  private int mLastTotalItemCount;
  @InjectView(R.id.gridview) GridView mGridView;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    mImageAdapter = new ImageAdapter(this);
    mLastTotalItemCount = 0;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_grid, container, false);
    ButterKnife.inject(this, view);
    mGridView.setAdapter(mImageAdapter);

    mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
      @Override public void onScrollStateChanged(AbsListView view, int scrollState) {

      }

      @Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
          int totalItemCount) {
        if (totalItemCount > mLastTotalItemCount && totalItemCount - firstVisibleItem < Photo_Scroll_Buffer) {
          Log.d("gridview", "Loading next page");
          EventBus.getDefault().post(new LoadNextPageEvent());
          mLastTotalItemCount += 100;
        }
      }
    });

    return view;
  }

  @OnItemClick(R.id.gridview) public void loadPhotoDetailView(int position) {
    EventBus.getDefault().post(new LoadPhotoDetailEvent(mImageAdapter.getItem(position)));
  }

  public void onEventMainThread(AddPhotoToGridEvent event) {
    mImageAdapter.add(event.getFhpxPhoto());
  }

  public void onEventMainThread(SearchEvent event) {
    mImageAdapter.clear();
    mLastTotalItemCount = 0;
  }

  public void onEventMainThread(AddPhotoSetToGridEvent event) {
    mImageAdapter.addAll(event.getFhpxPhotoList());
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }
}
