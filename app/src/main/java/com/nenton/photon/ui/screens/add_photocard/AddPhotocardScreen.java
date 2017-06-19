package com.nenton.photon.ui.screens.add_photocard;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.nenton.photon.BuildConfig;
import com.nenton.photon.R;
import com.nenton.photon.data.network.req.PhotocardReq;
import com.nenton.photon.data.storage.dto.ActivityResultDto;
import com.nenton.photon.data.storage.dto.FiltersDto;
import com.nenton.photon.data.storage.dto.PhotocardDto;
import com.nenton.photon.data.storage.dto.UserInfoDto;
import com.nenton.photon.data.storage.realm.PhotocardRealm;
import com.nenton.photon.data.storage.realm.StringRealm;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.sqopes.DaggerScope;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.flow.Screen;
import com.nenton.photon.mvp.model.MainModel;
import com.nenton.photon.mvp.presenters.AbstractPresenter;
import com.nenton.photon.mvp.presenters.RootPresenter;
import com.nenton.photon.ui.activities.RootActivity;
import com.nenton.photon.ui.screens.account.AccountScreen;
import com.nenton.photon.utils.ConstantsManager;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import dagger.Provides;
import flow.Flow;
import mortar.MortarScope;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by serge on 18.06.2017.
 */
@Screen(R.layout.screen_add_photocard)
public class AddPhotocardScreen extends AbstractScreen<RootActivity.RootComponent> {
    @Override
    public Object createScreenComponent(RootActivity.RootComponent parentComponent) {
        return DaggerAddPhotocardScreen_Component.builder()
                .rootComponent(parentComponent)
                .module(new Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        MainModel providePhotoModel() {
            return new MainModel();
        }

        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        AddPhotocardPresenter provideAccountPresenter() {
            return new AddPhotocardPresenter();
        }

        @Provides
        @DaggerScope(AddPhotocardScreen.class)
        AddPhotocardSelectTagsAdapter provideAddPhotocardSelectTagsAdapter() {
            return new AddPhotocardSelectTagsAdapter();
        }
    }

    @dagger.Component(dependencies = RootActivity.RootComponent.class, modules = Module.class)
    @DaggerScope(AddPhotocardScreen.class)
    public interface Component {
        void inject(AddPhotocardPresenter presenter);

        void inject(AddPhotocardView view);

        void inject(AddPhotocardSelectAlbumAdapter adapter);

        void inject(AddPhotocardSuggestionTagsAdapter adapter);

        Picasso getPicasso();

        RootPresenter getRootPresenter();
    }

    public class AddPhotocardPresenter extends AbstractPresenter<AddPhotocardView, MainModel> {

        private String mAvatarUri;
        Subscription subscribe;

        //region ========================= LifeCycle =========================

        @Override
        protected void initActionBar() {

        }

        @Override
        protected void initMenuPopup() {

        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            getView().initView();
            getView().showPhotoPanel();
        }

        @Override
        protected void onEnterScope(MortarScope scope) {
            super.onEnterScope(scope);
            subscribeOnActivityResult();
        }

        @Override
        protected void onExitScope() {
            subscribe.unsubscribe();
            super.onExitScope();
        }

        //endregion

        //region ========================= Gallery =========================

        public void chooseGallery() {
            if (getRootView() != null) {
                String[] permissions = new String[]{READ_EXTERNAL_STORAGE};
                if (mRootPresenter.checkPermissionsAndRequestIfNotGranted(permissions,
                        ConstantsManager.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
                    takePhotoFromGallery();
                }
            }
        }

        private void takePhotoFromGallery() {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT < 19) {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
            } else {
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
            }
            ((RootActivity) getRootView()).startActivityForResult(intent, ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE);
        }

        private void subscribeOnActivityResult() {
            Observable<ActivityResultDto> filter = mRootPresenter.getActivityResultSubject()
                    .filter(
                            activityResultDto -> activityResultDto.getResultCode() == Activity.RESULT_OK);
            subscribe = subscribe(filter, new ViewSubscriber<ActivityResultDto>() {
                @Override
                public void onNext(ActivityResultDto activityResultDto) {
                    handleActivityResult(activityResultDto);
                }
            });

        }


        private void handleActivityResult(ActivityResultDto activityResultDto) {
            if (activityResultDto.getRequestCode() == ConstantsManager.REQUEST_PROFILE_PHOTO_PICTURE && activityResultDto.getIntent() != null) {
                mAvatarUri = activityResultDto.getIntent().getData().toString();
                Uri parse = Uri.parse(mAvatarUri);
                File file = new File(getPath(getView().getContext(),parse));
                Uri uri = FileProvider.getUriForFile(((RootActivity) getRootView()).getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
                initPropertyView();
                getView().showPropertyPanel();
            } else {
                getRootView().showMessage("Что-то пошло не так");
            }
        }

        public String getPath(Context context, Uri uri) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else
                if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] {split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }

        public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = { column };
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        public boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        public  boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        public  boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public  boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }

        //endregion


        private void initPropertyView() {
            UserInfoDto userInfo = mModel.getUserInfo();
            mCompSubs.add(mModel.getUser(userInfo.getId())
                    .subscribe(userRealm -> {
                        getView().showView(userRealm);
                    }, throwable -> {
                        getRootView().showMessage("Ошибка");
                    }));
            mCompSubs.add(mModel.getPhotocardTagsObs().subscribe(s -> {
                getView().getTagsSuggestionAdapter().addTag(s);
            }, throwable -> {
            }));
        }

        public void clickAlbum(int positionOnSelectItem) {
            getView().checkedCurrentAlbum(positionOnSelectItem);
        }

        public void clickOnSuggestTag(String albumRealm) {
            getView().addTag(albumRealm);
        }

        public void savePhotocard() {
            FiltersDto filters = getView().getFilters();
            String namePhotocard = getView().getNamePhotocard();
            List<String> tags = getView().getTags();
            String idAlbum = getView().getIdAlbum();
            if (filters == null) {
                getRootView().showMessage("Не все фильтры выбраны");
                return;
            }
            if (namePhotocard == null) {
                getRootView().showMessage("Не выбрано имя фотокарточки");
                return;
            }
            if (tags == null) {
                getRootView().showMessage("Не выбрано ни одного тэга");
                return;
            }
            if (idAlbum == null) {
                getRootView().showMessage("Не выбран альбом");
                return;
            }

            if (mAvatarUri != null) {
                File file = new File(getPath(getView().getContext(), Uri.parse(mAvatarUri)));
                mModel.uploadPhotoToNetwork(mAvatarUri, file).subscribe(url -> {
                    PhotocardReq photocardReq = new PhotocardReq(idAlbum, namePhotocard, url, tags, filters);
                    mModel.createPhotocard(photocardReq).subscribe(id -> {
                        mModel.savePhotoToRealm(new PhotocardDto(id, namePhotocard, url, tags, filters));
                        Flow.get(getView().getContext()).set(new AccountScreen());
                    }, throwable -> {
                        getRootView().showMessage("Не получилось загрузить фотокарточку");
                    });
                }, throwable -> {
                    getRootView().showMessage("Не получилось загрузить фото");
                });

            }
        }
    }
}
