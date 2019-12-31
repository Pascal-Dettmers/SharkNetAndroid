//package com.htw.s0551733.sharnetpki.storage.database;
//
//import android.arch.persistence.db.SupportSQLiteDatabase;
//import android.arch.persistence.db.SupportSQLiteOpenHelper;
//import android.arch.persistence.room.Database;
//import android.arch.persistence.room.DatabaseConfiguration;
//import android.arch.persistence.room.InvalidationTracker;
//import android.arch.persistence.room.Room;
//import android.arch.persistence.room.RoomDatabase;
//import android.content.Context;
//import android.os.AsyncTask;
//import android.support.annotation.NonNull;
//
//import de.htw_berlin.s0551733.nfcchat.database.entity.Certification;
//import de.htw_berlin.s0551733.nfcchat.database.dataModel.CertificationDao;
//import de.htw_berlin.s0551733.nfcchat.database.dataModel.PublicKeyDao;
//import de.htw_berlin.s0551733.nfcchat.database.entity.PublicKeyPackage;
//
//@Database(entities = {Certification.class, PublicKeyPackage.class}, version = 1)
//public abstract class PKIRoomDatabase extends RoomDatabase {
//
//    private static volatile PKIRoomDatabase INSTANCE;
//
//    public abstract CertificationDao certificationDao();
//    public abstract PublicKeyDao publicKeyDao();
//
//
//    public static PKIRoomDatabase getDatabase(final Context context) {
//        if (INSTANCE == null) {
//            synchronized (PKIRoomDatabase.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            PKIRoomDatabase.class, "pki_database")
//                            .addCallback(sRoomDatabaseCallback)
//                            .allowMainThreadQueries()
//                            .build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//
//
//    @NonNull
//    @Override
//    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    protected InvalidationTracker createInvalidationTracker() {
//        return null;
//    }
//
//    @Override
//    public void clearAllTables() {
//
//    }
//
//    private static RoomDatabase.Callback sRoomDatabaseCallback =
//            new RoomDatabase.Callback(){
//
//                @Override
//                public void onOpen (@NonNull SupportSQLiteDatabase db){
//                    super.onOpen(db);
//                    new PopulateDbAsync(INSTANCE).execute();
//                }
//            };
//
//    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
//
//        private final CertificationDao CertificationDao;
//
//        PopulateDbAsync(PKIRoomDatabase db) {
//            CertificationDao = db.certificationDao();
//        }
//
//        @Override
//        protected Void doInBackground(final Void... params) {
//
//            return null;
//        }
//
//
//    }
//}
