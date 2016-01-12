package com.auth0.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that holds the information of a user's profile
 */
public class UserProfile extends com.auth0.java.core.UserProfile implements Parcelable {

    public UserProfile(com.auth0.java.core.UserProfile userProfile) {
        super(userProfile);
    }

    public UserProfile(Map<String, Object> values) {
        super(values);
    }

    @SuppressWarnings("unchecked")
    protected UserProfile(Parcel in) {
        id = in.readString();
        name = in.readString();
        nickname = in.readString();
        email = in.readString();
        pictureURL = in.readString();
        long tmpCreatedAt = in.readLong();
        createdAt = tmpCreatedAt != -1 ? new Date(tmpCreatedAt) : null;
        if (in.readByte() == 0x01) {
            identities = new ArrayList<>();
            in.readList(identities, UserIdentity.class.getClassLoader());
        } else {
            identities = null;
        }
        if (in.readByte() == 0x01) {
            extraInfo = (Map<String, Object>) in.readSerializable();
        } else {
            extraInfo = new HashMap<>();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(email);
        dest.writeString(pictureURL);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1L);
        if (identities == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(identities);
        }
        if (extraInfo == null) {
            dest.writeByte((byte) 0x00);
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeSerializable(new HashMap<>(extraInfo));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };
}
