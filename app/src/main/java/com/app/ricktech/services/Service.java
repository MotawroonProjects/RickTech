package com.app.ricktech.services;

import com.app.ricktech.models.BrandDataModel;
import com.app.ricktech.models.PlaceGeocodeData;
import com.app.ricktech.models.PlaceMapDetailsData;
import com.app.ricktech.models.ProductDataModel;
import com.app.ricktech.models.SingleProductModel;
import com.app.ricktech.models.SliderModel;
import com.app.ricktech.models.StatusResponse;
import com.app.ricktech.models.UserModel;

import retrofit2.Call;
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


}

