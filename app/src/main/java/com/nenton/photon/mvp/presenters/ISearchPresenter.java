package com.nenton.photon.mvp.presenters;

import java.util.Set;

/**
 * Created by serge on 04.06.2017.
 */

public interface ISearchPresenter {
    void clickOnSearch(CharSequence query);
    void clickOnStringQuery(String s);
    void goBack();
}
