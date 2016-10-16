package ru.flashrainbow.gameofthrones.data.storage.models;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.House;

import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDao;
import ru.flashrainbow.gameofthrones.data.storage.models.HouseDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig characterDaoConfig;
    private final DaoConfig houseDaoConfig;

    private final CharacterDao characterDao;
    private final HouseDao houseDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        characterDaoConfig = daoConfigMap.get(CharacterDao.class).clone();
        characterDaoConfig.initIdentityScope(type);

        houseDaoConfig = daoConfigMap.get(HouseDao.class).clone();
        houseDaoConfig.initIdentityScope(type);

        characterDao = new CharacterDao(characterDaoConfig, this);
        houseDao = new HouseDao(houseDaoConfig, this);

        registerDao(Character.class, characterDao);
        registerDao(House.class, houseDao);
    }
    
    public void clear() {
        characterDaoConfig.clearIdentityScope();
        houseDaoConfig.clearIdentityScope();
    }

    public CharacterDao getCharacterDao() {
        return characterDao;
    }

    public HouseDao getHouseDao() {
        return houseDao;
    }

}
