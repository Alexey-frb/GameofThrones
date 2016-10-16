package ru.flashrainbow.gameofthrones.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ru.flashrainbow.gameofthrones.R;
import ru.flashrainbow.gameofthrones.data.storage.models.Character;
import ru.flashrainbow.gameofthrones.data.storage.models.House;
import ru.flashrainbow.gameofthrones.utils.CircleTransform;
import ru.flashrainbow.gameofthrones.utils.ConstantManager;

public class CharacterAdapter extends RecyclerView.Adapter<CharacterAdapter.UserViewHolder> {

    private static final String TAG = ConstantManager.TAG_PREFIX + "CharacterAdapter";

    private List<Character> mCharacters;
    private House mHouse;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;

    public CharacterAdapter(List<Character> characterRes, House house, UserViewHolder.CustomClickListener customClickListener) {
        mCharacters = characterRes;
        mHouse = house;
        mCustomClickListener = customClickListener;
    }

    @Override
    public CharacterAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_character, parent, false);

        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(final CharacterAdapter.UserViewHolder holder, int position) {
        final Character character = mCharacters.get(position);

        Resources resources = mContext.getResources();

        // Определяем иконку какого дома отображать
        int iconResourceId = 0;
        String str = mHouse.getUrl();
        str = str.substring(str.lastIndexOf('/') + 1, str.length());

        switch (Integer.valueOf(str)) {
            case ConstantManager.STARK_ID_HOUSES:
                iconResourceId = R.drawable.stark_icon;
                break;
            case ConstantManager.LANNISTER_ID_HOUSES:
                iconResourceId = R.drawable.lanister_icon;
                break;
            case ConstantManager.TARGARYEN_ID_HOUSES:
                iconResourceId = R.drawable.targarien_icon;
                break;
        }

        // Иконка дома
        Picasso.with(mContext)
                .load(iconResourceId)
                .placeholder(resources.getDrawable(iconResourceId))
                .error(resources.getDrawable(iconResourceId))
                .fit()
                .centerCrop()
                .transform(new CircleTransform())
                .into(holder.mIconHouse);

        // Имя персонажа
        holder.mCharacterName.setText(character.getName());
        // Звания, титулы персонажа
        if (character.getTitle() != "") {
            holder.mCharacterWords.setText(character.getTitle());
        } else if (character.getAliases() != "") {
            holder.mCharacterWords.setText(character.getAliases());
        } else {
            holder.mCharacterWords.setText(mHouse.getWords());
        }
    }

    @Override
    public int getItemCount() {
        return mCharacters.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mIconHouse;
        private TextView mCharacterName;
        private TextView mCharacterWords;
        private LinearLayout mItem;

        private CustomClickListener mListener;

        @SuppressWarnings("deprecation")
        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);

            mIconHouse = (ImageView) itemView.findViewById(R.id.icon_house_img);
            mCharacterName = (TextView) itemView.findViewById(R.id.name_txt);
            mCharacterWords = (TextView) itemView.findViewById(R.id.words_txt);
            mItem = (LinearLayout) itemView.findViewById(R.id.item);

            mListener = customClickListener;
            mItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onUserItemClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onUserItemClickListener(int position);
        }
    }
}
