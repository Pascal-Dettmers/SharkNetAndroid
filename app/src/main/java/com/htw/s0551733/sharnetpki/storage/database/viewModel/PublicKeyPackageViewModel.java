//package com.htw.s0551733.sharnetpki.storage.database.viewModel;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//
//import java.util.List;
//
//import de.htw_berlin.s0551733.nfcchat.database.entity.PublicKeyPackage;
//import de.htw_berlin.s0551733.nfcchat.database.dataModel.PublicKeyRepository;
//
//public class PublicKeyPackageViewModel extends AndroidViewModel {
//
//    private PublicKeyRepository publicKeyRepository;
//    private LiveData<List<PublicKeyPackage>> allPublicKeys;
//
//
//    public PublicKeyPackageViewModel(@NonNull Application application) {
//        super(application);
//        publicKeyRepository = new PublicKeyRepository(application);
//        allPublicKeys = publicKeyRepository.getAllPublicKeysLiveData();
//    }
//
//    public LiveData<List<PublicKeyPackage>> getAllPublicKeys() {
//        return allPublicKeys;
//    }
//
//    public void deletePublicKey(PublicKeyPackage publicKeyPackage) {
//        publicKeyRepository.deletePublicKey(publicKeyPackage);
//    }
//
//    public void insert(PublicKeyPackage publicKeyPackage) {
//        publicKeyRepository.insertPublicKey(publicKeyPackage);
//    }
//}
