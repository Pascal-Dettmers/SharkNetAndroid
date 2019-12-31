//package com.htw.s0551733.sharnetpki.storage.database.dataModel;
//
//
//import android.app.Application;
//import android.arch.lifecycle.LiveData;
//import android.os.AsyncTask;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import de.htw_berlin.s0551733.nfcchat.database.entity.Certification;
//import de.htw_berlin.s0551733.nfcchat.database.PKIRoomDatabase;
//
//public class CertificationRepository {
//
//    private CertificationDao certificationDao;
//    private LiveData<List<Certification>> allCertifications;
//
//
//    public CertificationRepository(Application application) {
//        PKIRoomDatabase db = PKIRoomDatabase.getDatabase(application);
//        certificationDao = db.certificationDao();
//        allCertifications = certificationDao.getAllCertificationsLiveData();
//    }
//
//    public LiveData<List<Certification>> getAllCertificationsForUI() {
//        return allCertifications;
//    }
//
//    public ArrayList<Certification> getAllCertifications() {
//        List<Certification> allCertifications = certificationDao.getAllCertifications();
//        ArrayList<Certification> list = new ArrayList<>();
//
//        if (allCertifications != null) {
//            list.addAll(allCertifications);
//            return list;
//        } else {
//            return null;
//        }
//    }
//
//
//    public void insertCertification(Certification certification) {
//        new insertCertificationAsyncTask(certificationDao).execute(certification);
//    }
//
//    public void deleteCertification(Certification certification) {
//        new deleteCertificationAsyncTask(certificationDao).execute(certification);
//    }
//
//    private static class insertCertificationAsyncTask extends AsyncTask<Certification, Void, Void> {
//
//        private CertificationDao asyncTaskDao;
//
//        insertCertificationAsyncTask(CertificationDao dao) {
//            asyncTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Certification... params) {
//            asyncTaskDao.insertCertification(params[0]);
//            return null;
//        }
//    }
//
//    private static class deleteCertificationAsyncTask extends AsyncTask<Certification, Void, Void> {
//        private CertificationDao asynchTaskDao;
//
//        deleteCertificationAsyncTask(CertificationDao dao) {
//            asynchTaskDao = dao;
//        }
//
//        @Override
//        protected Void doInBackground(final Certification... params) {
//            asynchTaskDao.deleteCertification(params[0]);
//            return null;
//        }
//    }
//
//}
