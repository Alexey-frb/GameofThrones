package ru.flashrainbow.gameofthrones.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.flashrainbow.gameofthrones.R;
import ru.flashrainbow.gameofthrones.data.managers.DataManager;
import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDTO;
import ru.flashrainbow.gameofthrones.data.storage.models.CharacterDao;
import ru.flashrainbow.gameofthrones.data.storage.models.DaoSession;
import ru.flashrainbow.gameofthrones.data.storage.models.House;
import ru.flashrainbow.gameofthrones.data.storage.models.HouseDao;
import ru.flashrainbow.gameofthrones.ui.adapters.CharacterAdapter;
import ru.flashrainbow.gameofthrones.utils.ConstantManager;

public class CharacterListActivity extends AppCompatActivity {

    @BindView(R.id.navigation_drawer)
    DrawerLayout mNavigationDrawer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.view_pager_container)
    ViewPager mViewPager;
    @BindView(R.id.tabs_layout)
    TabLayout mTabLayout;
    @BindView(R.id.navigation_view)
    NavigationView mNavigationView;

    private TextView mHouseName;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        ButterKnife.bind(this);

        setupToolbar();
        setupDrawer();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * Кнопка Назад - закрыть меню, если открыто
     */
    @Override
    public void onBackPressed() {
        if (mNavigationDrawer != null && mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            switch (mViewPager.getCurrentItem()) {
                case 0:
                    mNavigationView.setCheckedItem(R.id.house_starks_menu);
                    mHouseName.setText(getString(R.string.house_name_starks));
                    break;
                case 1:
                    mNavigationView.setCheckedItem(R.id.house_lannisters_menu);
                    mHouseName.setText(getString(R.string.house_name_lannisters));
                    break;
                case 2:
                    mNavigationView.setCheckedItem(R.id.house_targaryen_menu);
                    mHouseName.setText(getString(R.string.house_name_targaryens));
                    break;
            }
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Инициализировать тулбар
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.author_name);
        }
    }
    /**
     * Выполнить настройки меню
     */
    private void setupDrawer() {
        mHouseName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.house_name_txt);

        if (mNavigationView != null) {
            mNavigationView.setCheckedItem(R.id.house_starks_menu);

            // Инициализация меню navigation view
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    item.setChecked(true);

                    switch (item.getItemId()) {
                        case R.id.house_starks_menu:
                            mViewPager.setCurrentItem(0);
                            break;

                        case R.id.house_lannisters_menu:
                            mViewPager.setCurrentItem(1);
                            break;

                        case R.id.house_targaryen_menu:
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.exit_menu:
                            finish();
                            break;
                    }
                    mNavigationDrawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String TAG = "CharacterListActivity";

        private RecyclerView mRecyclerView;
        private CharacterAdapter mCharacterAdapter;

        private List<Character> mCharactersInHouse;
        private House mHouse;

        private DataManager mDataManager;
        private DaoSession mDaoSession;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_character_list, container, false);

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.character_list);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);

            mDataManager = DataManager.getInstance();
            mDaoSession = mDataManager.getDaoSession();

            //Загрузить данные о персонажах в зависимости от идентификатора дома
            int numPage = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (numPage) {
                case 1:
                    loadCharactersDataFromDb(ConstantManager.STARK_ID_HOUSES);
                    break;
                case 2:
                    loadCharactersDataFromDb(ConstantManager.LANNISTER_ID_HOUSES);
                    break;
                case 3:
                    loadCharactersDataFromDb(ConstantManager.TARGARYEN_ID_HOUSES);
                    break;
            }

            return rootView;
        }

        /**
         * Загрузить данные о персонажах из БД
         *
         * @param houseId - идентификатор дома
         */
        private void loadCharactersDataFromDb(int houseId) {
            mCharactersInHouse = new ArrayList<>();
            mHouse = new House();

            try {
                // Выборка из БД: персонажи дома по идентификатору
                mCharactersInHouse = mDaoSession.queryBuilder(Character.class)
                        .where(CharacterDao.Properties.Allegiances.like("%/" + houseId))
                        .build()
                        .list();
                // Выборка из БД: дом по идентификатору
                mHouse = mDaoSession.queryBuilder(House.class)
                        .where(HouseDao.Properties.Url.like("%/" + houseId))
                        .build()
                        .unique();
                // Отобразить выбранных персонажей
                showCharacter(mCharactersInHouse, mHouse);

                Log.d(TAG, "loadCharactersDataFromDb: ok");
            } catch (Exception e) {
                Log.e(TAG, "loadCharactersDataFromDb: " + e.getMessage());
                e.printStackTrace();
            }
        }

        /**
         * Отобразить выбранных персонажей
         *
         * @param characters - выборка с персонажами
         * @param house      - выборка с домом
         */
        private void showCharacter(final List<Character> characters, final House house) {
            mCharacterAdapter = new CharacterAdapter(characters, house, new CharacterAdapter.UserViewHolder.CustomClickListener() {
                @Override
                public void onUserItemClickListener(int position) {
                    Log.d(TAG, "onUserItemClickListener: ");
                    CharacterDTO characterDTO = new CharacterDTO(characters.get(position), house);
                    Intent profileIntent = new Intent(getActivity(), CharacterActivity.class);
                    profileIntent.putExtra(ConstantManager.PARCELABLE_KEY, characterDTO);

                    startActivity(profileIntent);
                }
            });

            mRecyclerView.swapAdapter(mCharacterAdapter, false);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.house_name_starks);
                case 1:
                    return getString(R.string.house_name_lannisters);
                case 2:
                    return getString(R.string.house_name_targaryens);
            }
            return null;
        }
    }
}