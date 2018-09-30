package com.appian.manchesterunitednews.data.account;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class UserAccount implements Parcelable {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String accessToken;
    private String avatar;

    private UserAccount() {
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        if(TextUtils.isEmpty(fullName)) {
            return firstName + " " + lastName;
        }
        return fullName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAuthorization() {
        return "Bearer "+accessToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(fullName);
        parcel.writeString(accessToken);
        parcel.writeString(avatar);
    }

    public static final Parcelable.Creator<UserAccount> CREATOR
            = new Parcelable.Creator<UserAccount>() {
        public UserAccount createFromParcel(Parcel in) {
            return new UserAccount(in);
        }

        public UserAccount[] newArray(int size) {
            return new UserAccount[size];
        }
    };

    private UserAccount(Parcel in) {
        id = in.readInt();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        fullName = in.readString();
        accessToken = in.readString();
        avatar = in.readString();
    }

    public static class Builder {
        private int id;
        private String email;
        private String firstName;
        private String lastName;
        private String fullName;
        private String accessToken;
        private String avatar;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public UserAccount build() {
            UserAccount userAccount = new UserAccount();
            userAccount.id = id;
            userAccount.email = email;
            userAccount.firstName = firstName;
            userAccount.lastName = lastName;
            userAccount.fullName = fullName;
            userAccount.accessToken = accessToken;
            userAccount.avatar = avatar;
            return userAccount;
        }
    }
}
