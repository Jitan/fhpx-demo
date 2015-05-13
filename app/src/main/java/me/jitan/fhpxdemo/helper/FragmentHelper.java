package me.jitan.fhpxdemo.helper;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.fragment.GridFragment;
import me.jitan.fhpxdemo.fragment.PhotoDetailFragment;

public class FragmentHelper
{
    private static String mLastVisibleFragment;
    private static PhotoDetailFragment mPhotoDetailFragment;
    private static GridFragment mGridFragment;
    private static FragmentManager mFragmentManager;
    private static ActionBar mActionBar;
    private static AppCompatActivity mActivity;
    public final static String PHOTO_DETAIL_FRAGMENT_TAG = "photo_detail_fragment";
    public final static String GRID_FRAGMENT_TAG = "grid_fragment";
    public static final String ACTIVE_FRAGMENT_KEY = "active_fragment_key";

    public static void setupFragments(AppCompatActivity activity, Bundle savedInstanceState)
    {
        mActivity = activity;
        mFragmentManager = activity.getSupportFragmentManager();
        mActionBar = activity.getSupportActionBar();

        if (savedInstanceState != null)
        {
            mLastVisibleFragment = savedInstanceState.getString(ACTIVE_FRAGMENT_KEY);
            mPhotoDetailFragment = (PhotoDetailFragment) mFragmentManager.findFragmentByTag
                    (PHOTO_DETAIL_FRAGMENT_TAG);
            mGridFragment = (GridFragment) mFragmentManager.findFragmentByTag(GRID_FRAGMENT_TAG);
        }
        else
        {
            mGridFragment = new GridFragment();
            mPhotoDetailFragment = new PhotoDetailFragment();

            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mGridFragment, GRID_FRAGMENT_TAG)
                    .add(R.id.fragment_container, mPhotoDetailFragment, PHOTO_DETAIL_FRAGMENT_TAG)
                    .commit();
        }

        if (mLastVisibleFragment == null || mLastVisibleFragment.equals(GRID_FRAGMENT_TAG))
        {
            showGridFragment();
        }
        else
        {
            showPhotoDetailFragment();
        }
        setFragmentBackstackListener();
    }

    private static void setFragmentBackstackListener()
    {
        mFragmentManager.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener()
                {
                    @Override
                    public void onBackStackChanged()
                    {
                        if (mGridFragment.isVisible())
                        {
                            mActionBar.show();
                            mActivity.setRequestedOrientation(ActivityInfo
                                    .SCREEN_ORIENTATION_PORTRAIT);
                        }
                    }
                });
    }

    public static void showGridFragment()
    {
        mActionBar.show();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFragmentManager.beginTransaction()
                .hide(mPhotoDetailFragment)
                .show(mGridFragment)
                .commit();
    }

    public static void showPhotoDetailFragment()
    {
        mActionBar.hide();
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mFragmentManager.beginTransaction()
                .hide(mGridFragment)
                .show(mPhotoDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    public static String getActiveFragmentTag()
    {
        if (mPhotoDetailFragment.isVisible())
        {
            return PHOTO_DETAIL_FRAGMENT_TAG;
        }
        else
        {
            return GRID_FRAGMENT_TAG;
        }
    }
}



