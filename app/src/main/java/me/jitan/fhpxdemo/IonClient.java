package me.jitan.fhpxdemo;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import me.jitan.fhpxdemo.model.FhpxPhoto;

public class IonClient
{
    private final static String FiveHundredPX_PhotoApi_BaseUrl = "https://api.500px.com/v1/photos/";
    private final static String FiveHundredPX_ConsumerKey =
            "&consumer_key=dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2";

    private final Context mContext;
    private static IonClient mInstance;
    private ImageAdapter mImageAdapter;

    private IonClient(Context context)
    {
        mContext = context.getApplicationContext();
    }

    public static IonClient getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new IonClient(context);
        }
        return mInstance;
    }

    public void loadSearchResults(String searchQuery, final ImageAdapter imageAdapter)
    {
        mImageAdapter = imageAdapter;

        Ion.with(mContext)
                .load(FiveHundredPX_PhotoApi_BaseUrl +
                        "/search?term=" +
                        searchQuery.trim() +
                        "&image_size[]=3&image_size[]=1080&rpp=100&sort=highest_rating" +
                        FiveHundredPX_ConsumerKey)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        mImageAdapter.clear();
                        new JsonToFphxImageTask().executeOnExecutor(AsyncTask
                                .THREAD_POOL_EXECUTOR,result);
                    }
                });

    }

    private final class JsonToFphxImageTask extends AsyncTask<JsonObject, FhpxPhoto,
            Void>
    {
        @Override
        protected Void doInBackground(JsonObject... params)
        {
            String authorName, thumbUrl = "", url = "";
            JsonObject userObject, photoObject;
            List<FhpxPhoto> fhpxPhotos = new ArrayList<>();
            JsonArray photos = params[0].getAsJsonArray("photos");

            for (JsonElement photo : photos)
            {
                photoObject = photo.getAsJsonObject();

                userObject = photoObject.get("user").getAsJsonObject();
                authorName = userObject.get("firstname").getAsString();
                authorName += " " + userObject.get("lastname").getAsString();

                JsonArray imageUrls = photoObject.get("images").getAsJsonArray();
                for (JsonElement imageUrl : imageUrls)
                {
                    JsonObject imageUrlObject = imageUrl.getAsJsonObject();
                    String size = imageUrlObject.get("size").getAsString();

                    switch (size)
                    {
                        case "3":
                            thumbUrl = imageUrlObject.get("url").getAsString();
                            break;
                        case "1080":
                            url = imageUrlObject.get("url").getAsString();
                    }
                }

                publishProgress(new FhpxPhoto(thumbUrl, url, authorName));
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(FhpxPhoto... params)
        {
            mImageAdapter.add(params[0]);
        }
    }
}
