package me.jitan.fhpxdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import me.jitan.fhpxdemo.event.AddPhotoSetToGridEvent;
import me.jitan.fhpxdemo.event.SearchEvent;
import me.jitan.fhpxdemo.model.FhpxPhoto;

public class IonClient
{
    private final static String PhotoApi_BaseUrl = "https://api.500px.com/v1/photos/";
    private final static String Fhpx_ConsumerKey =
            "&consumer_key=dNRpNAjucR4By3KM9HvFWgb4fa1rNArB6R2nBfv2";

    private final Context mContext;
    private static IonClient mInstance;

    private IonClient(Context context)
    {
        mContext = context.getApplicationContext();
        EventBus.getDefault().register(this);
    }

    public static IonClient getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new IonClient(context);
        }
        return mInstance;
    }

    public void onEvent(SearchEvent event)
    {
        Toast.makeText(mContext, "Searching for: " + event.getSearchQuery(), Toast
                .LENGTH_SHORT).show();

        Ion.with(mContext)
                .load(PhotoApi_BaseUrl +
                        "/search?term=" +
                        event.getSearchQuery() +
                        "&sort=" +
                        event.getSortOptions() +
                        "&image_size[]=3&image_size[]=1080&image_size[]=2048&rpp=100" +
                        Fhpx_ConsumerKey)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>()
                {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        new JsonToFphxImageTask().execute(result);
                    }
                });
    }

    private static final class JsonToFphxImageTask extends AsyncTask<JsonObject, FhpxPhoto,
            ArrayList<FhpxPhoto>>
    {
        @Override
        protected ArrayList<FhpxPhoto> doInBackground(JsonObject... params)
        {
            ArrayList<FhpxPhoto> fhpxPhotoSet = new ArrayList<>();

            String authorName = "", thumbUrl = "", url = "", largeUrl = "";
            JsonObject userObject, photoObject;
            JsonArray photos = params[0].getAsJsonArray("photos");

            for (JsonElement photo : photos)
            {
                photoObject = photo.getAsJsonObject();

                userObject = photoObject.get("user").getAsJsonObject();
                if (!userObject.get("firstname").isJsonNull())
                {
                    authorName = userObject.get("firstname").getAsString();
                }

                if (!userObject.get("lastname").isJsonNull())
                {
                    authorName += " " + userObject.get("lastname").getAsString();
                }

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
                        case "2080":
                            largeUrl = imageUrlObject.get("url").getAsString();
                    }
                }

                fhpxPhotoSet.add(new FhpxPhoto(thumbUrl, url, largeUrl, authorName));
            }
            return fhpxPhotoSet;
        }

        @Override
        protected void onPostExecute(ArrayList<FhpxPhoto> fhpxPhotoSet)
        {
            EventBus.getDefault().post(new AddPhotoSetToGridEvent(fhpxPhotoSet));
        }
    }
}
