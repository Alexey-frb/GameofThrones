package ru.flashrainbow.gameofthrones.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.redmadrobot.chronos.ChronosConnector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.flashrainbow.gameofthrones.R;
import ru.flashrainbow.gameofthrones.data.managers.DataManager;
import ru.flashrainbow.gameofthrones.data.network.res.CharacterRes;
import ru.flashrainbow.gameofthrones.data.network.res.HouseRes;
import ru.flashrainbow.gameofthrones.data.storage.SaveCharactersDataInDb;
import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;
import ru.flashrainbow.gameofthrones.data.storage.models.House;
import ru.flashrainbow.gameofthrones.utils.ConstantManager;
import ru.flashrainbow.gameofthrones.utils.NetworkStatusChecker;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";

    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    private ChronosConnector mConnector;

    private DataManager mDataManager;
    private DaoSession mDaoSession;

    private List<Character> listCharacters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.d(TAG, "onCreate");

        ButterKnife.bind(this);

        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);

        mDataManager = DataManager.getInstance();
        mDaoSession = mDataManager.getDaoSession();

        showProgress();

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            //Запрашиваем по сети информацию о трех домах
            getHouseDataFromNetwork(ConstantManager.TARGARYEN_ID_HOUSES);
            getHouseDataFromNetwork(ConstantManager.LANNISTER_ID_HOUSES);
            getHouseDataFromNetwork(ConstantManager.STARK_ID_HOUSES);
            //Запрашиваем по сети информацию о всех персонажах
            getCharactersDataFromNetwork(0);
        } else {
            startCharacterListActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        mConnector.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        mConnector.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        mConnector.onSaveInstanceState(outState);
    }

    /**
     * Отобразить снекбар с сообщением
     *
     * @param message
     */
    private void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Получить данные о доме по сети
     *
     * @param houseId - идентификатор дома
     */
    private void getHouseDataFromNetwork(int houseId) {
        Log.d(TAG, "getHouseDataFromNetwork #" + houseId);

        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<HouseRes> call = mDataManager.getHouseDataFromNetwork(houseId);
            call.enqueue(new Callback<HouseRes>() {
                @Override
                public void onResponse(Call<HouseRes> call, Response<HouseRes> response) {
                    try {
                        if (response.code() == 200) {
                            //Status: Ok
                            mDaoSession.getHouseDao().insertOrReplaceInTx(new House(response.body()));
                        } else {
                            showSnackbar(getString(R.string.error_not_response_from_server));
                            startCharacterListActivity();
                        }
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onResponse: getHouseDataFromNetwork " + e.toString());
                        showSnackbar(e.toString());
                        startCharacterListActivity();
                    }
                }

                @Override
                public void onFailure(Call<HouseRes> call, Throwable t) {
                    Log.e(TAG, "onFailure: getHouseDataFromNetwork " + t.getMessage());
                    showSnackbar(t.getMessage());
                }
            });
        } else {
            showSnackbar(getString(R.string.error_network_not_available));
            startCharacterListActivity();
        }
    }

    /**
     * Получить данные о всех персонажах по сети
     *
     * @param pageNum - номер страницы от 1 до 43
     */
    private void getCharactersDataFromNetwork(int pageNum) {
        final int pageId = pageNum + 1;

        if (pageId > ConstantManager.NUMBER_OF_REQUEST) {
            Log.d(TAG, "getCharactersDataFromNetwork succeful");

            // Сохранить данные в БД в отдельном потоке
            mConnector.runOperation(new SaveCharactersDataInDb(listCharacters), false);
        } else {
            Log.d(TAG, "getCharactersDataFromNetwork, page #" + pageId);

            if (NetworkStatusChecker.isNetworkAvailable(this)) {
                Call<List<CharacterRes>> call = mDataManager.getCharactersDataFromNetwork(pageId);
                call.enqueue(new Callback<List<CharacterRes>>() {
                    @Override
                    public void onResponse(Call<List<CharacterRes>> call, Response<List<CharacterRes>> response) {
                        try {
                            if (response.code() == 200) {
                                //Status: Ok
                                for (CharacterRes characterRes : response.body()) {
                                    listCharacters.add(new Character(characterRes));
                                }
                                //Запрос следующей страницы с данными о персонажах
                                getCharactersDataFromNetwork(pageId);
                            } else {
                                showSnackbar(getString(R.string.error_not_response_from_server));
                                startCharacterListActivity();
                            }
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onResponse: getCharactersDataFromNetwork " + e.toString());
                            showSnackbar(e.toString());
                            startCharacterListActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CharacterRes>> call, Throwable t) {
                        Log.e(TAG, "onFailure: getCharactersDataFromNetwork " + t.getMessage());
                        showSnackbar(t.getMessage());
                        startCharacterListActivity();
                    }
                });
            } else {
                showSnackbar(getString(R.string.error_network_not_available));
                startCharacterListActivity();
            }
        }
    }

    /**
     * Вызывается после завершения сохранения данных о персонажах в локальную базу данных
     *
     * @param result - результат
     */
    @SuppressWarnings("unused")
    public void onOperationFinished(final SaveCharactersDataInDb.Result result) {
        if (result.isSuccessful()) {
            Log.d(TAG, "onOperationFinished: SaveCharactersDataInDb successful!");

            startCharacterListActivity();
        } else {
            hideProgress();
            Log.e(TAG, "onOperationFinished: SaveCharactersDataInDb " + result.getErrorMessage());
            showSnackbar(result.getErrorMessage());
        }
    }

    private void startCharacterListActivity() {
        hideProgress();

        if (mDataManager.isDatabaseEmpty()) {
            showSnackbar(getString(R.string.error_database_is_empty));
        } else {
            Intent characterListIntent = new Intent(SplashActivity.this, CharacterListActivity.class);

            startActivity(characterListIntent);
            finish();
        }
    }
}
