package com.example.powerincode.bakingapp.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.powerincode.bakingapp.common.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe extends BaseModel implements Parcelable {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_SERVING = "servings";
    private static final String KEY_IMAGE = "image";

    public Long id;
    public String name;
    public List<Ingredient> ingredients;
    public List<Step> steps;
    public Integer servings;
    public String image;

    public Recipe() {
        super();
    }

    public Recipe(JSONObject json) {
        super(json);
    }

    @Override
    protected void parse(JSONObject json) throws JSONException {
        id = json.getLong(KEY_ID);
        name = json.getString(KEY_NAME);
        servings = json.getInt(KEY_SERVING);
        image = json.getString(KEY_IMAGE);
        ingredients = BaseModel.parseArray(Ingredient.class, json.getJSONArray(KEY_INGREDIENTS));
        steps = BaseModel.parseArray(Step.class, json.getJSONArray(KEY_STEPS));
    }

    protected Recipe(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
        servings = in.readByte() == 0x00 ? null : in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(name);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
        if (servings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(servings);
        }
        dest.writeString(image);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}