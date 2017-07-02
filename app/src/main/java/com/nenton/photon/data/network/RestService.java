package com.nenton.photon.data.network;


import com.nenton.photon.data.network.req.AlbumCreateReq;
import com.nenton.photon.data.network.req.AlbumEditReq;
import com.nenton.photon.data.network.req.PhotoIdReq;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.network.req.UserCreateReq;
import com.nenton.photon.data.network.req.UserEditReq;
import com.nenton.photon.data.network.req.UserLoginReq;
import com.nenton.photon.data.network.res.Album;
import com.nenton.photon.data.network.res.IdRes;
import com.nenton.photon.data.network.res.Photocard;
import com.nenton.photon.data.network.res.SuccessRes;
import com.nenton.photon.data.network.res.UploadPhotoRes;
import com.nenton.photon.data.network.res.SignUpRes;
import com.nenton.photon.data.network.res.UserEditRes;
import com.nenton.photon.data.network.res.UserInfo;
import com.nenton.photon.data.network.res.SignInRes;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RestService {

    //region ========================= Users =========================

    // get User info
    @GET("user/{userId}")
    Observable<Response<UserInfo>> getUserInfoObs(@Path("userId") String userId);

    // edit user info
    @PUT("user/{userId}")
    Observable<Response<UserEditRes>> editUserInfoObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Body UserEditReq user);

    // create user
    @POST("user/signUp")
    Observable<Response<SignUpRes>> signUp(@Body UserCreateReq user);

    // login user
    @POST("user/signIn")
    Observable<Response<SignInRes>> signIn(@Body UserLoginReq user);

    //upload image photo
    @Multipart
    @POST("user/{userId}/image/upload")
    Observable<Response<UploadPhotoRes>> uploadPhoto(@Header("Authorization")String authToken, @Path("userId") String userId, @Part MultipartBody.Part file);

    //endregion

    //region ========================= Photocard =========================

    // get photocard list
    @GET("photocard/list")
    Observable<Response<List<Photocard>>> getPhotocardListObs(@Query("limit") int limit, @Query("offset") int offset);

    // get tags
    @GET("photocard/tags")
    Observable<Response<List<String>>> getTagsObs();

    // get photocard
    @GET("user/{userId}/photocard/{id}")
    Observable<Response<Photocard>> getPhotocardObs(@Header("If-Modified-Since")String date, @Path("userId") String userId, @Path("id") String id);

    // create photocard
    @POST("user/{userId}/photocard/")
    Observable<Response<IdRes>> createPhotocardObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Body PhotocardReq photocard);

    // edit photocard
    @PUT("user/{userId}/photocard/{id}")
    Observable<Response<Photocard>> editPhotocardObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Path("id") String id, @Body PhotocardReq photocard);

    // delete photocard
    @DELETE("user/{userId}/photocard/{id}")
    Observable<Response> deletePhotocardObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Path("id") String id);

    //add views
    @POST("photocard/{photocardId}/view")
    Observable<Response<SuccessRes>> addPhotocardViewsObs(@Path("photocardId") String photocardId, @Body PhotoIdReq id);

    //endregion

    //region ========================= Album =========================

    // add to favorite
    @POST("user/{userId}/favorite/{photocardId}")
    Observable<Response<SuccessRes>> addPhotocardFavObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Path("photocardId") String photocardId);

    //delete photocard from favorite
    @DELETE("user/{userId}/favorite/{photocardId}")
    Observable<Response> deletePhotocardFavObs (@Header("Authorization")String authToken, @Path("userId") String userId, @Path("photocardId") String photocardId);

    //get album list
    @GET("user/{userId}/album/list")
    Observable<Response<List<Album>>> getAlbumListObs(@Path("userId") String userId, @Query("limit") int limit, @Query("offset") int offset);

    // get album
    @GET("user/{userId}/album/{id}")
    Observable<Response<Album>> getAlbumObs(@Path("userId") String userId, @Path("id") String id);

    // create album
    @POST("user/{userId}/album/")
    Observable<Response<Album>> createAlbumObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Body AlbumCreateReq album);

    // edit album
    @PUT("user/{userId}/album/{id}")
    Observable<Response<Album>> editAlbumObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Path("id") String id, @Body AlbumEditReq album);

    // delete album
    @DELETE("user/{userId}/album/{id}")
    Observable<Response<Object>> deleteAlbumObs(@Header("Authorization")String authToken, @Path("userId") String userId, @Path("id") String id);

    //endregion
}
