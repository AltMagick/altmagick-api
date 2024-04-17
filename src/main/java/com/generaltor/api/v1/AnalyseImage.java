package com.generaltor.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generaltor.api.v1.entity.Sub;
import com.generaltor.api.v1.helper.SubHelper;
import com.generaltor.api.v1.helper.VertexImage;
import com.generaltor.api.v1.mapper.AnalyseImageResponse;
import com.generaltor.api.v1.mapper.ErrorResponse;
import com.google.cloud.firestore.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.generaltor.api.v1.helper.JsonHelper.serializeToJson;

@Path("/api/v1/analyse")
@ApplicationScoped
public class AnalyseImage {
    private static final Logger LOG = Logger.getLogger(AnalyseImage.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    Firestore firestore;

    @ConfigProperty(name = "custom.vertex.project_id")
    String projectId;

    @ConfigProperty(name = "custom.vertex.location")
    String location;

    @ConfigProperty(name = "custom.vertex.model_name")
    String modelName;

    @ConfigProperty(name = "custom.vertex.prompt")
    String prompt;

    @ConfigProperty(name = "custom.secrets.maxallowedusagecount")
    int maxAllowedUsageCount;

    @POST
    @Consumes({"image/jpeg", "image/png", "image/gif", "image/bmp", "image/webp", "image/heic", "image/heif"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response analyseImage(@HeaderParam("Authorization") String license,
                                 @HeaderParam("X-ALT-Language") String language,
                                 InputStream imageStream) throws IOException {
        LocalDateTime startTime = LocalDateTime.now();

        if (license.isBlank()) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Missing or blank Authorization header");
            return Response.status(Response.Status.BAD_REQUEST).entity(serializeToJson(errorResponse)).build();
        }

        if (language.isBlank()) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Missing or blank Language header");
            return Response.status(Response.Status.BAD_REQUEST).entity(serializeToJson(errorResponse)).build();
        }

        byte[] imageData = IOUtils.toByteArray(imageStream);

        if (imageData.length == 0) {
            ErrorResponse errorResponse = new ErrorResponse(400, "Missing image data");
            return Response.status(Response.Status.BAD_REQUEST).entity(serializeToJson(errorResponse)).build();
        }

        try {
            SubHelper firestoreHelper = new SubHelper(firestore);
            DocumentSnapshot documentSnapshot = firestoreHelper.getSubByLicenseKey(license);
            Sub sub = documentSnapshot.toObject(Sub.class);
            String subId = documentSnapshot.getId();
            assert sub != null;
            int usageCount = sub.getUsageCount();

            if (usageCount >= maxAllowedUsageCount) {
                ErrorResponse errorResponse = new ErrorResponse(403, "Usage limit exceeded");
                LOG.info("Usage limit exceeded for sub: " + documentSnapshot.getId());
                return Response.status(Response.Status.FORBIDDEN).entity(serializeToJson(errorResponse)).build();
            }

            String transcription = VertexImage.analyseImage(projectId, location, modelName, prompt, imageStream, language);

            firestore.runTransaction((Transaction.Function<Void>) transaction -> {
                sub.setUsageCount(sub.getUsageCount() + 1);
                transaction.update(documentSnapshot.getReference(), "usageCount", sub.getUsageCount());
                return null;
            });

            LocalDateTime endTime = LocalDateTime.now();
            long timestamp = endTime.toInstant(ZoneOffset.UTC).getEpochSecond();
            long elapsedTime = Duration.between(startTime, endTime).toMillis();

            AnalyseImageResponse analyseImageResponse = new AnalyseImageResponse(
                    subId,
                    transcription,
                    elapsedTime,
                    timestamp,
                    sub.getUsageCount(),
                    language
            );

            LOG.info("Image analysed for sub: " + subId + ", in " + elapsedTime + "ms");

            return Response.ok(objectMapper.writeValueAsString(analyseImageResponse)).build();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().equals("License not found")) {
                ErrorResponse errorResponse = new ErrorResponse(404, "License not found");
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(serializeToJson(errorResponse))
                        .build();
            }
            LOG.error("Error analysing image", e);
            ErrorResponse errorResponse = new ErrorResponse(500, "Internal server error");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(serializeToJson(errorResponse)).build();
        }
    }
}
