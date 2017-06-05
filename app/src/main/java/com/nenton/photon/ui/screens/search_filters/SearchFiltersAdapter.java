package com.nenton.photon.ui.screens.search_filters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nenton.photon.di.DaggerService;
import com.nenton.photon.flow.AbstractScreen;
import com.nenton.photon.ui.screens.search_filters.filters.FilterScreen;
import com.nenton.photon.ui.screens.search_filters.search.SearchScreen;

import mortar.MortarScope;

/**
 * Created by serge on 05.06.2017.
 */

class SearchFiltersAdapter extends PagerAdapter{

    public SearchFiltersAdapter() {
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        AbstractScreen screen = null;
        switch (position){
            case 0:
                screen = new SearchScreen();
                break;
            case 1:
                screen = new FilterScreen();
                break;
        }
        MortarScope mortarScope = createScreenScopeFromContext(container.getContext(), screen);
        Context screenContext = mortarScope.createContext(container.getContext());
        View newView = LayoutInflater.from(screenContext).inflate(screen.getLayoutResId(), container, false);
        container.addView(newView);
        return newView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Поиск";
                break;
            case 1:
                title = "Фильтры";
                break;
        }
        return title;
    }

    private MortarScope createScreenScopeFromContext(Context context, AbstractScreen screen){
        MortarScope parentScope = MortarScope.getScope(context);
        MortarScope childScope = parentScope.findChild(screen.getScopeName());

        if (childScope == null){
            Object screenComponent = screen.createScreenComponent(parentScope.getService(DaggerService.SERVICE_NAME));
            if (screenComponent == null){
                throw new IllegalStateException(" don't create screen component for " + screen.getScopeName());
            }

            childScope = parentScope.buildChild()
                    .withService(DaggerService.SERVICE_NAME, screenComponent)
                    .build(screen.getScopeName());
        }
        return childScope;
    }
}
