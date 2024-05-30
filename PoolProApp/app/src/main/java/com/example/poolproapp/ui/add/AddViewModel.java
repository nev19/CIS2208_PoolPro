package com.example.poolproapp.ui.add;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddViewModel extends ViewModel {
    private MutableLiveData<String> poolName = new MutableLiveData<>();

    private MutableLiveData<String> ownerName = new MutableLiveData<>();
    private MutableLiveData<Integer> capacity = new MutableLiveData<>();

    // Getter methods
    public LiveData<String> getPoolName() {
        return poolName;
    }
    public LiveData<String> getOwnerName() {
        return ownerName;
    }

    public LiveData<Integer> getCapacity() {
        return capacity;
    }

    // Setter methods to update
    public void setPoolName(String name) {
        poolName.setValue(name);
    }
    public void setOwnerName(String name) {
        ownerName.setValue(name);
    }

    public void setCapacity(int cap) {
        capacity.setValue(cap);
    }
}