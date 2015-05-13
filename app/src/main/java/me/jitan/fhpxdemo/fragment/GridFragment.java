package me.jitan.fhpxdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.ImageAdapter;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.event.AddPhotoToGridEvent;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;

public class GridFragment extends Fragment
{
    private ImageAdapter mImageAdapter;
    private static final EventBus eventBus = EventBus.getDefault();
    @InjectView(R.id.gridview) GridView mGridView;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mImageAdapter = new ImageAdapter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        ButterKnife.inject(this, view);
        mGridView.setAdapter(mImageAdapter);
        return view;
    }

    @OnItemClick(R.id.gridview)
    public void loadPhotoDetailView(int position)
    {
        eventBus.post(new LoadPhotoDetailEvent(mImageAdapter.getItem(position)));
    }


    public void onEventMainThread(AddPhotoToGridEvent event)
    {
        mImageAdapter.add(event.getFhpxPhoto());
    }

    public void onEventMainThread(SearchEvent event)
    {
        mImageAdapter.clear();
    }

    public void onEventMainThread(AddPhotoSetToGridEvent event)
    {
        mImageAdapter.addAll(event.getFhpxPhotoList());
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

    @Override public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
