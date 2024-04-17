package com.generaltor.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generaltor.api.v1.entity.Sub;
import com.generaltor.api.v1.helper.SubHelper;
import com.generaltor.api.v1.mapper.ErrorResponse;
import com.generaltor.api.v1.mapper.VerifyLicenseResponse;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import javax.swing.text.Document;

import static com.generaltor.api.v1.helper.JsonHelper.serializeToJson;

@Path("/api/v1/license")
@ApplicationScoped
public class VerifyLicense {
    private static final Logger LOG = Logger.getLogger(VerifyLicense.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Inject
    Firestore firestore;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyLicense(@HeaderParam("Authorization") String license) {
        if (license == null || license.isBlank()) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Missing or blank Authorization header");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(serializeToJson(errorResponse))
                    .build();
        }

        try {
            SubHelper firestoreHelper = new SubHelper(firestore);
            DocumentSnapshot documentSnapshot = firestoreHelper.getSubByLicenseKey(license);
            Sub sub = documentSnapshot.toObject(Sub.class);
            String subId = documentSnapshot.getId();
            VerifyLicenseResponse verifyLicenseResponse = new VerifyLicenseResponse(subId, sub.getUserName(), sub.getUserEmail());
            LOG.info("License verified from sub: " + subId);
            return Response.ok(objectMapper.writeValueAsString(verifyLicenseResponse)).build();

        } catch (Exception e) {
            if (e.getMessage().equals("License not found")) {
                ErrorResponse errorResponse = new ErrorResponse(404, "License not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(serializeToJson(errorResponse))
                        .build();
            }
            LOG.error("Error verifying license", e);
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(serializeToJson(errorResponse))
                    .build();
        }
    }


}
