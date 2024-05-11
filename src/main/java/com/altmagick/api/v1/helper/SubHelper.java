package com.altmagick.api.v1.helper;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

public class SubHelper {

    private final Firestore firestore;

    public SubHelper(Firestore firestore) {
        this.firestore = firestore;
    }

    public DocumentSnapshot getSubByLicenseKey(String licenseKey) throws Exception {
        ApiFuture<QuerySnapshot> query = firestore.collection("subs").whereEqualTo("license.licenseKey", licenseKey).get();
        QuerySnapshot querySnapshot = query.get();

        if (querySnapshot.isEmpty()) {
            throw new Exception("License not found");
        }

        return querySnapshot.getDocuments().getFirst();
    }
}