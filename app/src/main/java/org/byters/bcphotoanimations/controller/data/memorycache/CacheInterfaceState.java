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
    public int getShowLastFrameNum() {
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
    public void setLastFrameShow(int num) {
        getData().setShowLastFrame(num);
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

    @Override
    public void changeStateGrid() {
        getData().setGridEnabled(!isGridEnabled());
        saveData();
    }

    @Override
    public void changeSettingsVisible() {
        getData().setSettingsVisible(!isSettingsVisible());
    }

    @Override
    public boolean isGridEnabled() {
        return getData().isGridEnabled();
    }

    @Override
    public boolean isSettingsVisible() {
        return getData().isSettingsVisible();
    }

    @Override
    public int getCameraType() {
        return getData().getCameraType();
    }

    @Override
    public void setCameraType(int cameraType) {
        getData().setCameraType(cameraType);
        saveData();
    }

}
