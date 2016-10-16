package ru.flashrainbow.gameofthrones.data.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.List;

import ru.flashrainbow.gameofthrones.data.managers.DataManager;
import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;

/**
 * Сохраняет в БД информацию о всех персонажах в отдельном потоке
 */
public class SaveCharactersDataInDb extends ChronosOperation<List<Character>> {

    private List<Character> mListCharacters;
    private DaoSession mDaoSession;

    public SaveCharactersDataInDb(List<Character> character) {
        mListCharacters = character;
        mDaoSession = DataManager.getInstance().getDaoSession();
    }

    @Nullable
    @Override
    public List<Character> run() {
        mDaoSession.getCharacterDao().insertOrReplaceInTx(mListCharacters);

        return null;
    }

    @NonNull
    @Override
    public Class<Result> getResultClass() {
        return Result.class;
    }

    public final static class Result extends ChronosOperationResult<List<Character>> {
    }
}
