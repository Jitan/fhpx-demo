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

public class IonClient
{
    private final Context mContext;
    private static IonClient INSTANCE;
    private ImageAdapter mImageAdapter;

    private IonClient(Context context)
    {
        mContext = context.getApplicationContext();
    }

    public static IonClient getInstance(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new IonClient(context);
        }
        return INSTANCE;
    }

    public void loadPopularImages(final ImageAdapter imageAdapter)
    {
        mImageAdapter = imageAdapter;

        Ion.with(mContext)
                .load("https://api.500px.com/v1/photos?feature=popular&sort=rating&image_size=3" +
                        "&consumer_key=dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        new JsonParserTask().execute(result);
                    }
                });
    }

    private final class JsonParserTask extends AsyncTask<JsonObject, Void, List<String>>
    {
        @Override
        protected List<String> doInBackground(JsonObject... params)
        {
            List<String> imageUrls = new ArrayList<>();
            JsonArray photos = params[0].getAsJsonArray("photos");

            for (JsonElement photo : photos)
            {
                String imageUrl = photo.getAsJsonObject().get("image_url").getAsString();
                imageUrls.add(imageUrl);
            }
            return imageUrls;
        }

        @Override
        protected void onPostExecute(List<String> imageUrls)
        {
            mImageAdapter.clear();
            mImageAdapter.addAll(imageUrls);
        }
    }
}
