package me.jitan.fhpxdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.data.PhotoService;
import me.jitan.fhpxdemo.event.CouldNotLoadImageEvent;
import me.jitan.fhpxdemo.event.LoadNextPageEvent;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;
import me.jitan.fhpxdemo.ui.helper.FragmentHelper;
import me.jitan.fhpxdemo.ui.helper.ToolbarHelper;

public class MainActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ToolbarHelper.setupToolbar(this);
    FragmentHelper.setupFragments(this, savedInstanceState);
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  public void onEventMainThread(SearchEvent event) {
    Toast.makeText(this, "Searching for: " + event.getSearchQuery(), Toast.LENGTH_SHORT).show();
    PhotoService.getInstance().doSearch(event.getSearchQuery(), event.getSortOption());
  }

  public void onEventMainThread(LoadNextPageEvent event) {
    Toast.makeText(this, "Loading more results..", Toast.LENGTH_SHORT).show();
    PhotoService.getInstance().loadNextPage();
  }

  public void onEventMainThread(LoadPhotoDetailEvent event) {
    FragmentHelper.showPhotoDetailFragment();
  }

  public void onEventMainThread(CouldNotLoadImageEvent event) {
    Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    outState.putString(FragmentHelper.ACTIVE_FRAGMENT_KEY, FragmentHelper.getActiveFragmentTag());
    super.onSaveInstanceState(outState);
  }

  @Override public void onStop() {
    EventBus.getDefault().unregister(this);
    super.onStop();
  }

  @Override public boolean onPrepareOptionsMenu(Menu menu) {
    ToolbarHelper.setSearchActionItem(menu.findItem(R.id.action_search));
    return super.onPrepareOptionsMenu(menu);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    return ToolbarHelper.triggerMenuAction(item);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }
}
