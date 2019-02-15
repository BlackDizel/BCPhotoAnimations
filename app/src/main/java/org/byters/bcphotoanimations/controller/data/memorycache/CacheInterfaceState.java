package org.byters.bcphotoanimations.controller.data.memorycache;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.model.ModelInterfaceState;
import org.byters.bcphotoanimations.model.PreferenceHelperEnum;

import javax.inject.Inject;

public class CacheInterfaceState implements ICacheInterfaceState {

    @Inject
    ICacheStorage cacheStorage;

    private ModelInterfaceState data;

    public CacheInterfaceState() {
        ApplicationStopMotion.getComponent().inject(this);
    }


    @Override
    public boolean isShowLastFrame() {
        return getData().isShowLastFrame();
    }

    private ModelInterfaceState getData() {
        if (data == null)
            data = cacheStorage.readObjectFromFile(cacheStorage.getAppFolder(), PreferenceHelperEnum.INTERFACE_STATE, ModelInterfaceState.class);
        if (data == null)
            data = new ModelInterfaceState();
        return data;
    }


    private void saveData() {
        cacheStorage.writeObjectToFile(data, cacheStorage.getAppFolder(), PreferenceHelperEnum.INTERFACE_STATE);
    }

    @Override
    public void setLastFrameShow(boolean isShow) {
        getData().setShowLastFrame(isShow);
        saveData();
    }

    @Override
    public boolean isFlashEnabled() {
        return getData().isFlashEnabled();
    }

    @Override
    public void changeStateFlash() {
        getData().setFlashEnabled(!isFlashEnabled());
        saveData();
    }

}
