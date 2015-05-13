package me.jitan.fhpxdemo.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.R;
import me.jitan.fhpxdemo.event.SearchEvent;

public class ToolbarHelper
{
    public static AppCompatActivity mActivity;
    private static MenuItem mSearchAction;
    private static Boolean mSearchOpened;
    private static String mSearchQuery, mSortOptions;
    private static Drawable mIconCloseSearch, mIconOpenSearch;
    private static EditText mSearchField;
    private static ActionBar mActionBar;

    public static void setupToolbar(AppCompatActivity activity)
    {
        mActivity = activity;

        Toolbar toolbar = ButterKnife.findById(mActivity, R.id.toolbar);
        if (toolbar != null)
        {
            mActivity.setSupportActionBar(toolbar);
        }
        mActionBar = mActivity.getSupportActionBar();

        mSearchField = ButterKnife.findById(mActivity, R.id.toolbar_edittext_search);
        mSearchField.addTextChangedListener(new SearchWatcher());
        setKeyboardSearchListener();
        mSearchOpened = false;
        mSearchQuery = "";

        // Non-deprecated form of getDrawable only available in API 21
        mIconCloseSearch = mActivity.getResources().getDrawable(R.drawable.ic_close);
        mIconOpenSearch = mActivity.getResources().getDrawable(R.drawable.ic_search);
    }

    public static void setSearchActionItem(MenuItem searchAction)
    {
        mSearchAction = searchAction;
    }

    public static boolean triggerMenuAction(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_search:
                toggleSearchFieldView();
                return true;

            case R.id.action_sort_rating:
                Toast.makeText(mActivity, "Sort on rating", Toast.LENGTH_SHORT).show();
                mSortOptions = "rating";
                loadSearchResults();
                return true;

            case R.id.action_sort_date:
                Toast.makeText(mActivity, "Sort on date", Toast.LENGTH_SHORT).show();
                mSortOptions = "created_at";
                loadSearchResults();
                return true;

            case R.id.action_sort_favorites:
                Toast.makeText(mActivity, "Sort on favorites", Toast.LENGTH_SHORT).show();
                mSortOptions = "favorites_count";
                loadSearchResults();
                return true;
        }

        return false;
    }

    private static void toggleSearchFieldView()
    {
        if (mSearchOpened)
        {
            closeSearchBar();
            hideKeyboard();
        }
        else
        {
            openSearchBar(mSearchQuery);
            showKeyboard();
        }
    }

    private static void closeSearchBar()
    {
        // Remove custom view.
        if (mActionBar != null)
        {
            mSearchField.setVisibility(View.GONE);
        }

        // Change search icon accordingly.
        mSearchAction.setIcon(mIconOpenSearch);
        mSearchOpened = false;
    }

    private static void openSearchBar(String queryText)
    {
        // Set custom view on action bar.
        if (mActionBar != null)
        {
            // Search edit text field setup.
            mSearchField.setVisibility(View.VISIBLE);
            mSearchField.setText(queryText);
            mSearchField.requestFocus();

            // Change search icon accordingly.
            mSearchAction.setIcon(mIconCloseSearch);
            mSearchOpened = true;
        }
    }

    private static void setKeyboardSearchListener()
    {
        mSearchField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    loadSearchResults();
                    return true;
                }
                return false;
            }
        });
    }

    private static void loadSearchResults()
    {
        ToolbarHelper.hideKeyboard();
        mSearchQuery = mSearchField.getText().toString().trim();

        if (!mSearchQuery.isEmpty())
        {
            EventBus.getDefault().post(
                    new SearchEvent(cleanSearchQuery(mSearchQuery), mSortOptions));
        }
    }

    private static String cleanSearchQuery(String searchQuery)
    {
        searchQuery = searchQuery.replaceAll("[^a-zA-Z0-9]+", "+").replaceAll("\\+$", "");
        return searchQuery;
    }

    public static void hideKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
    }

    public static void showKeyboard()
    {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        // only will trigger it if no physical keyboard is open
        inputMethodManager.showSoftInput(mSearchField, InputMethodManager.SHOW_IMPLICIT);
    }

    private static class SearchWatcher implements TextWatcher
    {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3)
        {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3)
        {

        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            mSearchQuery = mSearchField.getText().toString();
        }

    }
}
