package com.auth0.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Class that holds a user's token information.
 */
public class Token extends com.auth0.Token implements Parcelable {

    public Token(String idToken, String accessToken, String type, String refreshToken) {
        super(idToken, accessToken, type, refreshToken);
    }

    public Token(com.auth0.Token token) {
        super(token);
    }

    protected Token(Parcel in) {
        idToken = in.readString();
        accessToken = in.readString();
        type = in.readString();
        refreshToken = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idToken);
        dest.writeString(accessToken);
        dest.writeString(type);
        dest.writeString(refreshToken);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
