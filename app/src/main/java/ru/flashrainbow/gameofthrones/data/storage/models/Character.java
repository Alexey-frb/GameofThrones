package ru.flashrainbow.gameofthrones.data.storage.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import ru.flashrainbow.gameofthrones.data.network.res.CharacterRes;

@Entity(active = true, nameInDb = "Characters")
public class Character {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String url;

    @NotNull
    private String name;

    private String gender;
    private String culture;
    private String born;
    private String died;
    private String title;
    private String aliases;
    private String father;
    private String mother;
    private String allegiances;
    private String tvSeries;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 898307126)
    private transient CharacterDao myDao;

    public Character(CharacterRes character) {
        this.url = character.getUrl();
        this.name = character.getName();
        this.gender = character.getGender();
        this.culture = character.getCulture();
        this.born = character.getBorn();
        this.died = character.getDied();
        this.father = character.getFather();
        this.mother = character.getMother();

        if (character.getTitles().size() != 0) {
            this.title = character.getTitles().get(0);
            for (int i = 1; i < character.getTitles().size(); i++) {
                this.title = this.title + "\n" + character.getTitles().get(i);
            }
        } else {
            this.title = "";
        }

        if (character.getAliases().size() != 0) {
            this.aliases = character.getAliases().get(0);
            for (int i = 1; i < character.getAliases().size(); i++) {
                this.aliases = this.aliases + "\n" + character.getAliases().get(i);
            }
        } else {
            this.aliases = "";
        }

        if (character.getAllegiances().size() != 0) {
            this.allegiances = character.getAllegiances().get(0);
            for (int i = 1; i < character.getAllegiances().size(); i++) {
                this.allegiances = this.allegiances + "\n" + character.getAllegiances().get(i);
            }
        } else {
            this.allegiances = "";
        }

        if (character.getTvSeries().size() != 0) {
            this.tvSeries = character.getTvSeries().get(character.getTvSeries().size() - 1);
        } else {
            this.tvSeries = "";
        }
    }

    @Generated(hash = 588997965)
    public Character(Long id, @NotNull String url, @NotNull String name, String gender,
                     String culture, String born, String died, String title, String aliases,
                     String father, String mother, String allegiances, String tvSeries) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.gender = gender;
        this.culture = culture;
        this.born = born;
        this.died = died;
        this.title = title;
        this.aliases = aliases;
        this.father = father;
        this.mother = mother;
        this.allegiances = allegiances;
        this.tvSeries = tvSeries;
    }

    @Generated(hash = 1853959157)
    public Character() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCulture() {
        return this.culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getBorn() {
        return this.born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getDied() {
        return this.died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAliases() {
        return this.aliases;
    }

    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    public String getFather() {
        return this.father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return this.mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getAllegiances() {
        return this.allegiances;
    }

    public void setAllegiances(String allegiances) {
        this.allegiances = allegiances;
    }

    public String getTvSeries() {
        return this.tvSeries;
    }

    public void setTvSeries(String tvSeries) {
        this.tvSeries = tvSeries;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 162219484)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCharacterDao() : null;
    }
}
