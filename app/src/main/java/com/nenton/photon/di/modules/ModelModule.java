package com.nenton.photon.di.modules;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.utils.App;
import com.nenton.photon.utils.AppConfig;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule{
    @Provides
    @Singleton
    DataManager provideDataManager(){
        return DataManager.getInstance();
    }

    @Provides
    JobManager provideJobManager(){
        Configuration configuration = new Configuration.Builder(App.getContext())
                .minConsumerCount(AppConfig.MIN_CONSUMER_COUNT)
                .maxConsumerCount(AppConfig.MAX_CONSUMER_COUNT)
                .loadFactor(AppConfig.LOAD_FACTOR)
                .consumerKeepAlive(AppConfig.KEEP_ALIVE)
                .build();
        return new JobManager(configuration);
    }
}
