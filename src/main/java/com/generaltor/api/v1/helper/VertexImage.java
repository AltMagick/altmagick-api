package com.generaltor.api.v1.helper;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class VertexImage {

    public static String analyseImage(String projectId, String location, String modelName, String prompt, InputStream image, String language) throws IOException {
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);

            if (!image.markSupported()) {
                image = new BufferedInputStream(image);
            }

            String mimeType = getMimeType(image);

            byte[] imageData = IOUtils.toByteArray(image);

            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData(mimeType, imageData), prompt + "\nLanguage: " + language
            ));

            return ResponseHandler.getText(response).trim();
        }
    }

    private static String getMimeType(InputStream image) throws IOException {
        image.mark(10 * 1024);
        String mimeType = URLConnection.guessContentTypeFromStream(image);
        image.reset();
        return mimeType;
    }
}
