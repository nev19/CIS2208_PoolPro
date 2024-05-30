package com.example.poolproapp.ui.add_supplier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddSupplierViewModel extends ViewModel {
    private MutableLiveData<String> supplierEmail = new MutableLiveData<>();

    // Getter methods
    public LiveData<String> getSupplierEmail() {
        return supplierEmail;
    }

    // Setter methods to update
    public void setSupplierEmail(String email) {
        supplierEmail.setValue(email);
    }
}