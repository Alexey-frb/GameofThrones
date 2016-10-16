package ru.flashrainbow.gameofthrones.data.storage.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CharacterDTO implements Parcelable {
    private String mName;
    private String mBorn;
    private String mDied;
    private String mTitles;
    private String mAliases;
    private String mFather;
    private String mMother;
    private String mTvSeries;
    private String mWords;
    private String mUrlHouse;

    public CharacterDTO(Character characterData, House houseData) {
        mName = characterData.getName();
        mBorn = characterData.getBorn();
        mDied = characterData.getDied();
        mTitles = characterData.getTitle();
        mAliases = characterData.getAliases();
        mFather = characterData.getFather();
        mMother = characterData.getMother();
        mTvSeries = characterData.getTvSeries();
        mWords = houseData.getWords();
        mUrlHouse = houseData.getUrl();
    }

    protected CharacterDTO(Parcel in) {
        mName = in.readString();
        mBorn = in.readString();
        mDied = in.readString();
        mTitles = in.readString();
        mAliases = in.readString();
        mFather = in.readString();
        mMother = in.readString();
        mTvSeries = in.readString();
        mWords = in.readString();
        mUrlHouse = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mBorn);
        dest.writeString(mDied);
        dest.writeString(mTitles);
        dest.writeString(mAliases);
        dest.writeString(mFather);
        dest.writeString(mMother);
        dest.writeString(mTvSeries);
        dest.writeString(mWords);
        dest.writeString(mUrlHouse);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CharacterDTO> CREATOR = new Parcelable.Creator<CharacterDTO>() {
        @Override
        public CharacterDTO createFromParcel(Parcel in) {
            return new CharacterDTO(in);
        }

        @Override
        public CharacterDTO[] newArray(int size) {
            return new CharacterDTO[size];
        }
    };

    public String getName() {
        return mName;
    }

    public String getBorn() {
        return mBorn;
    }

    public String getDied() {
        return mDied;
    }

    public String getTitles() {
        return mTitles;
    }

    public String getAliases() {
        return mAliases;
    }

    public String getFather() {
        return mFather;
    }

    public String getMother() {
        return mMother;
    }

    public String getWords() {
        return mWords;
    }

    public String getUrlHouse() {
        return mUrlHouse;
    }

    public String getTvSeries() {
        return mTvSeries;
    }
}
