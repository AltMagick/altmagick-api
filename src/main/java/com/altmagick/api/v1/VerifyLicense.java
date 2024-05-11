package com.altmagick.api.v1;

import com.altmagick.api.v1.entity.Sub;
import com.altmagick.api.v1.helper.JsonHelper;
import com.altmagick.api.v1.helper.SubHelper;
import com.altmagick.api.v1.mapper.ErrorResponse;
import com.altmagick.api.v1.mapper.VerifyLicenseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

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
                    .entity(JsonHelper.serializeToJson(errorResponse))
                    .build();
        }

        try {
            SubHelper firestoreHelper = new SubHelper(firestore);
            DocumentSnapshot documentSnapshot = firestoreHelper.getSubByLicenseKey(license);
            Sub sub = documentSnapshot.toObject(Sub.class);
            String subId = documentSnapshot.getId();
            assert sub != null;
            VerifyLicenseResponse verifyLicenseResponse = new VerifyLicenseResponse(subId, sub.getUserName(), sub.getUserEmail(), sub.getCreatedAt(), sub.getRenewsAt(), sub.getEndAt(), sub.getStatus(), sub.isCancelled());
            LOG.info("License verified from sub: " + subId);
            return Response.ok(objectMapper.writeValueAsString(verifyLicenseResponse)).build();

        } catch (Exception e) {
            if (e.getMessage().equals("License not found")) {
                ErrorResponse errorResponse = new ErrorResponse(404, "License not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(JsonHelper.serializeToJson(errorResponse))
                        .build();
            }
            LOG.error("Error verifying license", e);
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(JsonHelper.serializeToJson(errorResponse))
                    .build();
        }
    }


}
