package com.auth0.core;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;


/**
 * Class with a Auth0 connection info
 */
public class Connection extends com.auth0.Connection implements Parcelable {

    public Connection(com.auth0.Connection connection) {
        super(connection);
    }

    public Connection(Map<String, Object> values) {
        super(values);
    }

    @SuppressWarnings("unchecked")
    protected Connection(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0x01) {
            values = (Map<String, Object>) in.readSerializable();
        } else {
            values = new HashMap<>();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (values == null || values.isEmpty()) {
            dest.writeByte((byte) 0x00);
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeSerializable(new HashMap<>(values));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Connection> CREATOR = new Parcelable.Creator<Connection>() {
        @Override
        public Connection createFromParcel(Parcel in) {
            return new Connection(in);
        }

        @Override
        public Connection[] newArray(int size) {
            return new Connection[size];
        }
    };
}
