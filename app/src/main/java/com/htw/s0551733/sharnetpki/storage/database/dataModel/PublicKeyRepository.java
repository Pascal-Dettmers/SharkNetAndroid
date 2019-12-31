//package com.htw.s0551733.sharnetpki.storage.database.dataModel;
//
//
//import android.app.Application;
//
//import androidx.lifecycle.LiveData;
//
//import com.htw.s0551733.sharnetpki.storage.database.PKIRoomDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import main.de.htw.berlin.s0551733.sharknetpki.interfaces.SharkNetPublicKey;
//
//public class PublicKeyRepository {
//
//    private PublicKeyDao publicKeyDao;
//    private LiveData<List<SharkNetPublicKey>> allPublicKeys;
//
//
//    //Todo
//    public PublicKeyRepository(Application application) {
//        PKIRoomDatabase db = PKIRoomDatabase.getDatabase(application);
//        publicKeyDao = db.publicKeyDao();
//        allPublicKeys = publicKeyDao.getAllPublicKeysLiveData();
//    }
//
//    public LiveData<List<PublicKeyPackage>> getAllPublicKeysLiveData() {
//        return allPublicKeys;
//    }
//
//    public ArrayList<PublicKeyPackage> getAllPublicKeys() {
//        List<PublicKeyPackage> allPublicKeys = publicKeyDao.getAllPublicKeys();
//        ArrayList<PublicKeyPackage> list = new ArrayList<>();
//
//        if (allPublicKeys != null) {
//            list.addAll(allPublicKeys);
//            return list;
//        } else {
//            return null;
//        }
//    }
//
//    public void insertPublicKey(PublicKeyPackage publicKeyPackage) {
//        new insertPublicKeyAsyncTask(publicKeyDao).execute(publicKeyPackage);
//    }
//
//    public void deletePublicKey(PublicKeyPackage publicKeyPackage) {
//        publicKeyDao.deletePublicKey(publicKeyPackage);
//    }
//
//    private static class insertPublicKeyAsyncTask extends AsyncTask<PublicKeyPackage, Void, Void> {
//
//        private PublicKeyDao asyncTaskDao;
//
//        insertPublicKeyAsyncTask(PublicKeyDao dao) {
//            asyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final PublicKeyPackage... params) {
//            asyncTaskDao.insertPublicKey(params[0]);
//            return null;
//        }
//    }
//
//
//    private static class deletePublicKeyAsyncTask extends AsyncTask<PublicKeyPackage, Void, Void> {
//        private PublicKeyDao asynchTaskDao;
//
//        deletePublicKeyAsyncTask(PublicKeyDao dao) {
//            asynchTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final PublicKeyPackage... params) {
//            asynchTaskDao.deletePublicKey(params[0]);
//            return null;
//        }
//    }
//}
