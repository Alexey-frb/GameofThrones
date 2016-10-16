package ru.flashrainbow.gameofthrones.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.flashrainbow.gameofthrones.data.network.res.CharacterRes;
import ru.flashrainbow.gameofthrones.data.network.res.HouseRes;

public interface RestService {

    @GET("houses/{houseId}")
    Call<HouseRes> getHouseData(@Path("houseId") int houseId);

    @GET("characters?pageSize=50")
    Call<List<CharacterRes>> getCharactersData(@Query("page") int pageId);
}
