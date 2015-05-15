package me.jitan.fhpxdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.koushikdutta.ion.Ion;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.event.LoadNextPageEvent;
import me.jitan.fhpxdemo.event.LoadPhotoDetailEvent;
import me.jitan.fhpxdemo.event.SearchEvent;
import me.jitan.fhpxdemo.helper.FragmentHelper;
import me.jitan.fhpxdemo.helper.ToolbarHelper;
import me.jitan.fhpxdemo.retrofit.FhpxPhotoService;

public class MainActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Uncomment below for Ion network logging
    //Ion.getDefault(this).configure().setLogging("fhpx-ion", Log.DEBUG);

    // Need to disable Spdy to access 500px API with Ion, otherwise we get weird errors.
    Ion.getDefault(this).getHttpClient().getSSLSocketMiddleware().setSpdyEnabled(false);

    ToolbarHelper.setupToolbar(this);
    FragmentHelper.setupFragments(this, savedInstanceState);
  }

  @Override public void onStart() {
    super.onStart();
    EventBus.getDefault().register(this);
  }

  public void onEventMainThread(LoadPhotoDetailEvent event) {
    FragmentHelper.showPhotoDetailFragment();
  }

  public void onEventMainThread(SearchEvent event) {
    //IonClient.getInstance(this).doSearch(event.getSearchQuery(), event.getSortOption());
    FhpxPhotoService.getInstance(this).doSearch(event.getSearchQuery(), event.getSortOption());
  }

  public void onEventMainThread(LoadNextPageEvent event) {
    //IonClient.getInstance(this).loadNextPage();
    FhpxPhotoService.getInstance(this).loadNextPage();
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
