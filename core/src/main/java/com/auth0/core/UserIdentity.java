package com.auth0.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that holds the information from a Identity Provider like Facebook or Twitter.
 */
public class UserIdentity extends com.auth0.java.core.UserIdentity implements Parcelable {

    @SuppressWarnings("unchecked")
    protected UserIdentity(Parcel in) {
        id = in.readString();
        connection = in.readString();
        provider = in.readString();
        social = in.readByte() != 0x00;
        accessToken = in.readString();
        accessTokenSecret = in.readString();
        if (in.readByte() == 0x01) {
            profileInfo = (Map<String, Object>) in.readSerializable();
        } else {
            profileInfo = new HashMap<>();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(connection);
        dest.writeString(provider);
        dest.writeByte((byte) (social ? 0x01 : 0x00));
        dest.writeString(accessToken);
        dest.writeString(accessTokenSecret);
        if (profileInfo == null) {
            dest.writeByte((byte) 0x00);
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeSerializable(new HashMap<>(profileInfo));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserIdentity> CREATOR = new Parcelable.Creator<UserIdentity>() {
        @Override
        public UserIdentity createFromParcel(Parcel in) {
            return new UserIdentity(in);
        }

        @Override
        public UserIdentity[] newArray(int size) {
            return new UserIdentity[size];
        }
    };
}
