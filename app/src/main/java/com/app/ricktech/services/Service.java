package com.app.ricktech.services;

import com.app.ricktech.models.AccessoryDataModel;
import com.app.ricktech.models.AddCompareModel;
import com.app.ricktech.models.AddToBuildDataModel;
import com.app.ricktech.models.BrandDataModel;
import com.app.ricktech.models.CategoryBuildingDataModel;
import com.app.ricktech.models.NotificationCount;
import com.app.ricktech.models.NotificationDataModel;
import com.app.ricktech.models.PlaceGeocodeData;
import com.app.ricktech.models.PlaceMapDetailsData;
import com.app.ricktech.models.ProductDataModel;
import com.app.ricktech.models.SavedProductDataModel;
import com.app.ricktech.models.SettingDataModel;
import com.app.ricktech.models.SingleProductModel;
import com.app.ricktech.models.SliderModel;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.SuggestionBrandDataModel;
import com.app.ricktech.models.SuggestionGameDataModel;
import com.app.ricktech.models.SuggestionsDataModel;
import com.app.ricktech.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> login(@Field("username") String username,
                          @Field("password") String password
    );

    @GET("api/sliders")
    Call<SliderModel> getSlider(@Header("lang") String lang);


    @FormUrlEncoded
    @POST("api/singleProduct")
    Call<SingleProductModel> getProductById(@Header("lang") String lang,
                                            @Field("product_id") String product_id
    );

    @GET("api/resendEmailVerificationCode")
    Call<UserModel> sendSmsCode(@Header("lang") String lang,
                                @Header("Authorization") String bearer_token
    );

    @FormUrlEncoded
    @POST("api/register")
    Call<UserModel> signUp(@Field("username") String username,
                           @Field("password") String password,
                           @Field("email") String email,
                           @Field("software_type") String software_type
    );

    @GET("api/logout")
    Call<StatusResponse> logout(@Header("Authorization") String bearer_token);

    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<StatusResponse> updateFirebaseToken(@Field("user_id") int user_id,
                                             @Field("firebase_token") String firebase_token,
                                             @Field("software_type") String software_type
    );

    @FormUrlEncoded
    @POST("api/confirmEmail")
    Call<UserModel> confirmEmail(@Header("Authorization") String bearer_token,
                                 @Field("code") String code
    );

    @FormUrlEncoded
    @POST("api/checkEmailForForgetPasswordReset")
    Call<UserModel> checkEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST("api/checkPasswordResetCode")
    Call<UserModel> checkEmailForgetPasswordValidCode(@Header("Authorization") String bearer_token,
                                                      @Field("code") String code);


    @FormUrlEncoded
    @POST("api/resetPassword")
    Call<UserModel> resetPassword(@Header("Authorization") String bearer_token,
                                  @Field("password") String password);


    @GET("api/getBrandsOfGamingPcs")
    Call<BrandDataModel> getGamingBrand(@Header("lang") String lang);


    @FormUrlEncoded
    @POST("api/getGamingProductsBYBrandId")
    Call<ProductDataModel> getGamingProductByBrandId(@Header("lang") String lang,
                                                     @Field("brand_id") String brand_id
    );


    @GET("api/getPcBuildingsCategories")
    Call<CategoryBuildingDataModel> getCategoryBuilding(@Header("lang") String lang
    );

    @FormUrlEncoded
    @POST("api/getProductsByCategoryId")
    Call<ProductDataModel> getProductByCategoryIdBuilding(@Header("lang") String lang,
                                                          @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST("api/getSubCategoriesByCategoryId")
    Call<CategoryBuildingDataModel> getSubCategoryBuilding(@Header("lang") String lang,
                                                           @Field("parent_id") String parent_id
    );

    @POST("api/savePcBuildings")
    Call<StatusResponse> addToBuild(@Header("lang") String lang,
                                    @Header("Authorization") String bearer_token,
                                    @Body AddToBuildDataModel model
    );


    @POST("api/comparePcBuildingWithGames")
    Call<SuggestionGameDataModel> compare(@Header("lang") String lang,
                                          @Body AddCompareModel model
    );

    @GET("api/getAccessories")
    Call<AccessoryDataModel> getAccessory(@Header("lang") String lang
    );


    @FormUrlEncoded
    @POST("api/compareGamingPcWithGames")
    Call<SuggestionGameDataModel> compareGaming(@Header("lang") String lang,
                                                @Field("product_id") String product_id
    );

    @GET("api/getSuggestionPcBuildingTypes")
    Call<SuggestionBrandDataModel> getSuggestionBrand(@Header("lang") String lang
    );


    @FormUrlEncoded
    @POST("api/getBrandsOfSuggestionPcBuildings")
    Call<BrandDataModel> getSuggestionBrands(@Header("lang") String lang,
                                             @Field("pc_building_type_id") String pc_building_type_id
    );


    @FormUrlEncoded
    @POST("api/getCategoriesOfSuggestionPcBuilding")
    Call<SuggestionsDataModel> getCategorySuggestions(@Header("lang") String lang,
                                                      @Field("brand_id") String brand_id,
                                                      @Field("pc_building_type_id") String pc_building_type_id
    );


    @FormUrlEncoded
    @POST("api/getSubCategoriesOfSuggestionPcBuilding")
    Call<SuggestionsDataModel> getSubCategorySuggestions(@Header("lang") String lang,
                                                         @Field("brand_id") String brand_id,
                                                         @Field("pc_building_type_id") String pc_building_type_id,
                                                         @Field("category_id") String category_id
    );

    @GET("api/getOffers")
    Call<ProductDataModel> getOffers(@Header("lang") String lang
    );

    @GET("api/allNotifications")
    Call<NotificationDataModel> getNotification(@Header("lang") String lang,
                                                @Header("Authorization") String bearer_token
    );

    @GET("api/unReadNotificationsCount")
    Call<NotificationCount> getUnreadNotificationCount(@Header("lang") String lang,
                                                       @Header("Authorization") String bearer_token
    );

    @GET("api/appInfo")
    Call<SettingDataModel> getSetting(@Header("lang") String lang);

    @GET("api/getSavedPcBuildings")
    Call<SavedProductDataModel> getSavedBuilding(@Header("lang") String lang,
                                                 @Header("Authorization") String bearer_token
    );

    @FormUrlEncoded
    @POST("api/getDetailsOfSinglePcOrder")
    Call<SuggestionsDataModel> getSavedBuildingDetails(@Header("lang") String lang,
                                                       @Field("pc_id") String pc_id
    );


    @FormUrlEncoded
    @POST("api/getSubCategoriesOfPcBuilding")
    Call<SuggestionsDataModel> getSubCategorySuggestionsSavedBuildings(@Header("lang") String lang,
                                                                       @Field("pc_id") String pc_id,
                                                                       @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST("api/pcBuildingDeleting")
    Call<StatusResponse> deleteSavedBuilding(@Header("lang") String lang,
                                             @Field("pc_id") String pc_id
    );


}

