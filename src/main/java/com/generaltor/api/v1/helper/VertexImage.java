package com.generaltor.api.v1.helper;

import com.generaltor.api.v1.entity.ImageRequest;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.apache.tika.Tika;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class VertexImage {

    public static String analyseImage(String projectId, String location, String modelName, String prompt, ImageRequest imageRequest, String language) throws IOException {
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);

            URL url = new URL(imageRequest.getImageUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:124.0) Gecko/20100101 Firefox/124.0");
            InputStream urlStream = connection.getInputStream();            PushbackInputStream pushUrlStream = new PushbackInputStream(urlStream, 100);
            byte[] firstBytes = new byte[100];
            pushUrlStream.read(firstBytes);
            pushUrlStream.unread(firstBytes);

            Tika tika = new Tika();
            ByteArrayInputStream bais = new ByteArrayInputStream(firstBytes);
            String mimeType = tika.detect(bais);
            String formatName = mimeType.startsWith("image/") ? mimeType.substring("image/".length()) : null;

            List<String> acceptedFormats = Arrays.asList("jpeg", "png", "webp", "heic", "heif");

            BufferedImage image = ImageIO.read(pushUrlStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            if (!acceptedFormats.contains(formatName)) {
                ImageIO.write(image, "JPEG", byteArrayOutputStream);
                formatName = "jpeg";
            } else {
                ImageIO.write(image, formatName, byteArrayOutputStream);
            }

            byte[] imageData = byteArrayOutputStream.toByteArray();

            GenerateContentResponse response = model.generateContent(ContentMaker.fromMultiModalData(
                    PartMaker.fromMimeTypeAndData("image/" + formatName.toLowerCase(), imageData), prompt + "\nLanguage: " + language
            ));

            return ResponseHandler.getText(response).trim();
        }
    }
}