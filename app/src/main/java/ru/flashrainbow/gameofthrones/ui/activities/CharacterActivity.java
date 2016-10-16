package ru.flashrainbow.gameofthrones.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.flashrainbow.gameofthrones.R;
import ru.flashrainbow.gameofthrones.data.managers.DataManager;
import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDTO;
import ru.flashrainbow.gameofthrones.utils.ConstantManager;

public class CharacterActivity extends AppCompatActivity {

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
    TextView mCharacterFather;
    @BindView(R.id.character_mother_txt)
    TextView mCharacterMother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        ButterKnife.bind(this);

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
        CharacterDTO characterDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);

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

        // Имя персонажа
        mCollapsingToolbarLayout.setTitle(characterDTO.getName());
        // Лозунг
        mCharacterWords.setText(characterDTO.getWords());
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
            mCharacterFather.setText(characterDTO.getFather());
        } else {
            mCharacterFather.setText(R.string.no_data);
        }
        // Мать
        if (!characterDTO.getMother().equals("")) {
            mCharacterMother.setText(characterDTO.getMother());
        } else {
            mCharacterMother.setText(R.string.no_data);
        }

        // Если персонаж умер, то выводим номер сезона
        if (!characterDTO.getDied().equals("") && !characterDTO.getTvSeries().equals("")) {
            showSnackbar("died in " + characterDTO.getTvSeries());
        }
    }
}
