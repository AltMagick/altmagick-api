package com.generaltor.api.v1.helper;

import com.generaltor.api.v1.entity.ImageRequest;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class VertexImage {

    public static String analyseImage(String projectId, String location, String modelName, String prompt, ImageRequest imageRequest, String language) throws IOException {
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);

            BufferedImage image;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (imageRequest.getImageUrl().startsWith("data:image/")) {
                String base64Image = imageRequest.getImageUrl().split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            } else {
                URL url = new URL(imageRequest.getImageUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.5414.117 Mobile Safari/537.36");
                InputStream urlStream = connection.getInputStream();
                image = ImageIO.read(urlStream);
            }

            BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
            boolean canWrite = ImageIO.write(convertedImage, "JPEG", byteArrayOutputStream);
            if (!canWrite) {
                throw new IllegalStateException("Failed to write image.");
            }
            byte[] imageData = byteArrayOutputStream.toByteArray();

            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData("image/jpeg", imageData), prompt + "\nLanguage: " + language
            ));

            return ResponseHandler.getText(response).trim();
        }
    }
}