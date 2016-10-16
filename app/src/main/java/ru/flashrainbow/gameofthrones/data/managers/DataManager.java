package ru.flashrainbow.gameofthrones.data.managers;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import ru.flashrainbow.gameofthrones.data.network.RestService;
import ru.flashrainbow.gameofthrones.data.network.ServiceGenerator;
import ru.flashrainbow.gameofthrones.data.network.res.CharacterRes;
import ru.flashrainbow.gameofthrones.data.network.res.HouseRes;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;
import ru.flashrainbow.gameofthrones.utils.DevApplication;

public class DataManager {
    private static DataManager INSTANCE = null;

    private PreferencesManager mPreferencesManager;
    private Context mContext;

    private RestService mRestService;
    private DaoSession mDaoSession;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = DevApplication.getContext();

        mRestService = ServiceGenerator.createService(RestService.class);
        mDaoSession = DevApplication.getDaoSession();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }

    public RestService getRestService() {
        return mRestService;
    }

    //region --- Network ---
    public Call<HouseRes> getHouseDataFromNetwork(int houseId) {
        return mRestService.getHouseData(houseId);
    }

    public Call<List<CharacterRes>> getCharactersDataFromNetwork(int pageId) {
        return mRestService.getCharactersData(pageId);
    }
    //endregion

    //region --- Database ---
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
    //endregion
}
