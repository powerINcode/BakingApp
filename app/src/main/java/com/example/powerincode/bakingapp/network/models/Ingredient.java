package com.example.powerincode.bakingapp.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.powerincode.bakingapp.common.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient extends BaseModel implements Parcelable {
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_INGREDIENT = "ingredient";

    public Long quantity;
    public String measure;
    public String ingredient;

    public Ingredient() {
    }

    public Ingredient(JSONObject json) {
        super(json);
    }

    @Override
    protected void parse(JSONObject json) throws JSONException {
        quantity = json.getLong(KEY_QUANTITY);
        measure = json.getString(KEY_MEASURE);
        ingredient = json.getString(KEY_INGREDIENT);
    }

    protected Ingredient(Parcel in) {
        quantity = in.readByte() == 0x00 ? null : in.readLong();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(quantity);
        }
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}