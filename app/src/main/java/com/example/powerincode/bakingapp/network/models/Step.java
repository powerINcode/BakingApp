package com.example.powerincode.bakingapp.network.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.powerincode.bakingapp.common.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

public class Step extends BaseModel implements Parcelable {
    private static final String KEY_ID = "id";
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_VIDEO_URL = "videoURL";
    private static final String KEY_THUMBNAIL_URL = "thumbnailURL";

    public Integer id;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    public Step() {
        super();
    }

    public Step(JSONObject json) {
        super(json);
    }

    @Override
    protected void parse(JSONObject json) throws JSONException {
        id = json.getInt(KEY_ID);
        shortDescription = json.getString(KEY_SHORT_DESCRIPTION);
        description = json.getString(KEY_DESCRIPTION);
        videoURL = json.getString(KEY_VIDEO_URL);
        thumbnailURL = json.getString(KEY_THUMBNAIL_URL);
    }

    protected Step(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
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
            dest.writeInt(id);
        }
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}