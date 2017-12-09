package com.example.powerincode.bakingapp.common.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by powerman23rus on 02.12.17.
 * Enjoy ;)
 */

public abstract class BaseModel {
    private static final String sTAG = BaseModel.class.getSimpleName();
    public final String TAG = BaseModel.this.getClass().getSimpleName();

    public static <T extends  BaseModel> ArrayList<T> parseArray(Class<T> modelClass, JSONArray jsonArray) {
        ArrayList<T> result = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                T model = modelClass.getDeclaredConstructor(JSONObject.class).newInstance(jsonObject);
                result.add(model);
            } catch (JSONException e) {
                Log.e(sTAG, e.getLocalizedMessage());
            } catch (IllegalAccessException e) {
                Log.e(sTAG, e.getLocalizedMessage());
            } catch (InstantiationException e) {
                Log.e(sTAG, e.getLocalizedMessage());
            } catch (NoSuchMethodException e) {
                Log.e(sTAG, e.getLocalizedMessage());
            } catch (InvocationTargetException e) {
                Log.e(sTAG, e.getLocalizedMessage());
            }
        }

        return result;
    }
    public BaseModel(JSONObject json) {
        try {
            parse(json);
        } catch (JSONException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    public BaseModel() {
    }

    protected abstract void parse(JSONObject json) throws JSONException;
}
