package ru.flashrainbow.gameofthrones.data.storage.models;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import ru.flashrainbow.gameofthrones.data.network.res.HouseRes;

@Entity(active = true, nameInDb = "Houses")
public class House {

    @Id
    private Long id;

    @NotNull
    @Unique
    private String url;

    private String name;
    private String region;
    private String coatOfArms;
    private String words;
    private String titles;
    private String seats;
    private String currentLord;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1167916919)
    private transient HouseDao myDao;

    public House(HouseRes house) {
        this.url = house.getUrl();
        this.name = house.getName();
        this.region = house.getRegion();
        this.coatOfArms = house.getCoatOfArms();
        this.words = house.getWords();
        this.currentLord = house.getCurrentLord();

        if (house.getTitles().size() != 0) {
            this.titles = house.getTitles().get(0);
            for (int i = 1; i < house.getTitles().size(); i++) {
                this.titles = this.titles + "\n" + house.getTitles().get(i);
            }
        } else {
            this.titles = "";
        }

        if (house.getSeats().size() != 0) {
            this.seats = house.getSeats().get(0);
            for (int i = 1; i < house.getSeats().size(); i++) {
                this.seats = this.seats + "\n" + house.getSeats().get(i);
            }
        } else {
            this.seats = "";
        }
    }

    @Generated(hash = 1523723832)
    public House(Long id, @NotNull String url, String name, String region,
                 String coatOfArms, String words, String titles, String seats,
                 String currentLord) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.region = region;
        this.coatOfArms = coatOfArms;
        this.words = words;
        this.titles = titles;
        this.seats = seats;
        this.currentLord = currentLord;
    }

    @Generated(hash = 389023854)
    public House() {
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

    public String getRegion() {
        return this.region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCoatOfArms() {
        return this.coatOfArms;
    }

    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public String getWords() {
        return this.words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getTitles() {
        return this.titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public String getSeats() {
        return this.seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getCurrentLord() {
        return this.currentLord;
    }

    public void setCurrentLord(String currentLord) {
        this.currentLord = currentLord;
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
    @Generated(hash = 451323429)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHouseDao() : null;
    }
}
