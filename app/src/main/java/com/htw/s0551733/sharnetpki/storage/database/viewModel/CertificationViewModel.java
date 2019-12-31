//package com.htw.s0551733.sharnetpki.storage.database.viewModel;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//
//import java.util.List;
//
//import de.htw_berlin.s0551733.nfcchat.database.dataModel.CertificationRepository;
//import de.htw_berlin.s0551733.nfcchat.database.entity.Certification;
//
//public class CertificationViewModel extends AndroidViewModel {
//
//    private CertificationRepository certificationRepository;
//
//    private LiveData<List<Certification>> allCertifications;
//
//
//    public CertificationViewModel(@NonNull Application application) {
//        super(application);
//        certificationRepository = new CertificationRepository(application);
//        allCertifications = certificationRepository.getAllCertificationsForUI();
//    }
//
//    public LiveData<List<Certification>> getAllCertifications() { return allCertifications; }
//
//    public void deleteCertification(Certification certification) {certificationRepository.deleteCertification(certification);}
//
//    public void insert(Certification certification) { certificationRepository.insertCertification(certification); }
//
//
//}
