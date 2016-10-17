package ru.flashrainbow.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.flashrainbow.gameofthrones.R;
import ru.flashrainbow.gameofthrones.data.managers.DataManager;
import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDTO;
import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDao;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;
import ru.flashrainbow.gameofthrones.utils.ConstantManager;

public class CharacterActivity extends AppCompatActivity {

    private static final String TAG = "CharacterActivity";

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.house_icon_img)
    ImageView mHouseIcon;
    @BindView(R.id.character_born_txt)
    TextView mCharacterBorn;
    @BindView(R.id.character_died_txt)
    TextView mCharacterDied;
    @BindView(R.id.character_words_txt)
    TextView mCharacterWords;
    @BindView(R.id.character_titles_txt)
    TextView mCharacterTitles;
    @BindView(R.id.character_aliases_txt)
    TextView mCharacterAliases;
    @BindView(R.id.character_father_txt)
    TextView mCharacterFatherTxt;
    @BindView(R.id.character_mother_txt)
    TextView mCharacterMotherTxt;
    @BindView(R.id.character_father_btn)
    Button mCharacterFather;
    @BindView(R.id.character_mother_btn)
    Button mCharacterMother;

    private Character mCharacter;

    private DaoSession mDaoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        ButterKnife.bind(this);

        mDaoSession = DataManager.getInstance().getDaoSession();

        setupToolbar();
        initProfileData();
    }

    /**
     * Инициализировать тулбар
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
     * Загрузить данные о персонаже
     */
    private void initProfileData() {
        final CharacterDTO characterDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

        // Определяем герб какого дома загрузить
        int iconResourceId = 0;
        String str = characterDTO.getUrlHouse();
        str = str.substring(str.lastIndexOf('/') + 1, str.length());

        switch (Integer.valueOf(str)) {
            case ConstantManager.STARK_ID_HOUSES:
                iconResourceId = R.drawable.stark;
                break;
            case ConstantManager.LANNISTER_ID_HOUSES:
                iconResourceId = R.drawable.lannister;
                break;
            case ConstantManager.TARGARYEN_ID_HOUSES:
                iconResourceId = R.drawable.targarien;
                break;
        }

        // Герб дома
        Picasso.with(DataManager.getInstance().getContext())
                .load(iconResourceId)
                .placeholder(iconResourceId)
                .error(iconResourceId)
                .fit()
                .centerCrop()
                .into(mHouseIcon);

        // Лозунг
        mCharacterWords.setText(characterDTO.getWords());
        // Имя персонажа
        mCollapsingToolbarLayout.setTitle(characterDTO.getName());
        // Дата рождения
        if (!characterDTO.getBorn().equals("")) {
            mCharacterBorn.setText(characterDTO.getBorn());
        } else {
            mCharacterBorn.setText(R.string.no_data);
        }
        // Дата смерти
        if (!characterDTO.getDied().equals("")) {
            mCharacterDied.setText(characterDTO.getDied());
        } else {
            mCharacterDied.setText(R.string.no_data);
        }
        // Титулы
        if (!characterDTO.getTitles().equals("")) {
            mCharacterTitles.setText(characterDTO.getTitles());
        } else {
            mCharacterTitles.setText(R.string.no_data);
        }
        // Звания
        if (!characterDTO.getAliases().equals("")) {
            mCharacterAliases.setText(characterDTO.getAliases());
        } else {
            mCharacterAliases.setText(R.string.no_data);
        }
        // Отец
        if (!characterDTO.getFather().equals("")) {
            String strFather = characterDTO.getFather();
            strFather = strFather.substring(strFather.lastIndexOf('/') + 1, strFather.length());
            final Character characterFather = loadCharacterFromDb(Integer.valueOf(strFather));

            mCharacterFather.setVisibility(View.VISIBLE);
            mCharacterFatherTxt.setVisibility(View.VISIBLE);
            mCharacterFather.setText(characterFather.getName());
            mCharacterFather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParents(characterFather);
                }
            });
        } else {
            mCharacterFather.setVisibility(View.INVISIBLE);
            mCharacterFatherTxt.setVisibility(View.INVISIBLE);
        }
        // Мать
        if (!characterDTO.getMother().equals("")) {
            String strMother = characterDTO.getMother();
            strMother = strMother.substring(strMother.lastIndexOf('/') + 1, strMother.length());
            final Character characterMother = loadCharacterFromDb(Integer.valueOf(strMother));

            mCharacterMother.setVisibility(View.VISIBLE);
            mCharacterMotherTxt.setVisibility(View.VISIBLE);
            mCharacterMother.setText(characterMother.getName());
            mCharacterMother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParents(characterMother);
                }
            });
        } else {
            mCharacterMother.setVisibility(View.INVISIBLE);
            mCharacterMotherTxt.setVisibility(View.INVISIBLE);
        }

        // Если персонаж умер, то выводим номер сезона
        if (!characterDTO.getDied().equals("") && !characterDTO.getTvSeries().equals("")) {
            showSnackbar("died in " + characterDTO.getTvSeries());
        }
    }

    /**
     * Загрузить данные о персонаже из БД
     *
     * @param characterId - идентификатор дома
     */
    private Character loadCharacterFromDb(int characterId) {
        mCharacter = new Character();
        Log.d(TAG, "loadCharacterFromDb: " + characterId);
        try {
            // Выборка из БД: персонажи дома по идентификатору
            mCharacter = mDaoSession.queryBuilder(Character.class)
                    .where(CharacterDao.Properties.Url.like("%/" + characterId))
                    .build()
                    .unique();

            Log.d(TAG, "loadCharacterFromDb: ok");

            return mCharacter;
        } catch (Exception e) {
            Log.e(TAG, "loadCharacterFromDb: " + e.getMessage());
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Отобразить данные о родителе персонажа
     * @param character - персонаж
     */
    private void showParents(Character character) {
        // Имя персонажа
        mCollapsingToolbarLayout.setTitle(character.getName());
        // Дата рождения
        if (!character.getBorn().equals("")) {
            mCharacterBorn.setText(character.getBorn());
        } else {
            mCharacterBorn.setText(R.string.no_data);
        }
        // Дата смерти
        if (!character.getDied().equals("")) {
            mCharacterDied.setText(character.getDied());
        } else {
            mCharacterDied.setText(R.string.no_data);
        }
        // Титулы
        if (!character.getTitle().equals("")) {
            mCharacterTitles.setText(character.getTitle());
        } else {
            mCharacterTitles.setText(R.string.no_data);
        }
        // Звания
        if (!character.getAliases().equals("")) {
            mCharacterAliases.setText(character.getAliases());
        } else {
            mCharacterAliases.setText(R.string.no_data);
        }
        // Отец
        if (!character.getFather().equals("")) {
            String strFather = character.getFather();
            strFather = strFather.substring(strFather.lastIndexOf('/') + 1, strFather.length());
            final Character characterFather = loadCharacterFromDb(Integer.valueOf(strFather));

            mCharacterFather.setVisibility(View.VISIBLE);
            mCharacterFatherTxt.setVisibility(View.VISIBLE);
            mCharacterFather.setText(characterFather.getName());
            mCharacterFather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParents(characterFather);
                }
            });
        } else {
            mCharacterFather.setVisibility(View.INVISIBLE);
            mCharacterFatherTxt.setVisibility(View.INVISIBLE);
        }
        // Мать
        if (!character.getMother().equals("")) {
            String strMother = character.getMother();
            strMother = strMother.substring(strMother.lastIndexOf('/') + 1, strMother.length());
            final Character characterMother = loadCharacterFromDb(Integer.valueOf(strMother));

            mCharacterMother.setVisibility(View.VISIBLE);
            mCharacterMotherTxt.setVisibility(View.VISIBLE);
            mCharacterMother.setText(characterMother.getName());
            mCharacterMother.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showParents(characterMother);
                }
            });
        } else {
            mCharacterMother.setVisibility(View.INVISIBLE);
            mCharacterMotherTxt.setVisibility(View.INVISIBLE);
        }

        // Если персонаж умер, то выводим номер сезона
        if (!character.getDied().equals("") && !character.getTvSeries().equals("")) {
            showSnackbar("died in " + character.getTvSeries());
        }
    }
}
