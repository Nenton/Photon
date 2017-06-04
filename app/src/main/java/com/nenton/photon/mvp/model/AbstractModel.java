package com.nenton.photon.mvp.model;

import com.birbit.android.jobqueue.JobManager;
import com.nenton.photon.data.managers.DataManager;
import com.nenton.photon.di.DaggerService;
import com.nenton.photon.di.components.DaggerModelComponent;
import com.nenton.photon.di.components.ModelComponent;
import com.nenton.photon.di.modules.ModelModule;


import javax.inject.Inject;

public abstract class AbstractModel {
    @Inject
    DataManager mDataManager;
    @Inject
    JobManager mJobManager;

    public AbstractModel() {
        ModelComponent component = DaggerService.getComponent(ModelComponent.class);
        if (component == null){
            component = createDaggerModelComponent();
            DaggerService.registerComponent(ModelComponent.class, component);
        }
        component.inject(this);
    }

    public AbstractModel(DataManager dataManager, JobManager jobManager) {
        mDataManager = dataManager;
        mJobManager = jobManager;
    }

    private ModelComponent createDaggerModelComponent() {
        return DaggerModelComponent.builder()
                .modelModule(new ModelModule())
                .build();
    }
}
