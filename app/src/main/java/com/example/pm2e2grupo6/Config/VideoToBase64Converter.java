package com.example.pm2e2grupo6.Config;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class VideoToBase64Converter {

    // Método para convertir un archivo de video a Base64
    public static String videoToBase64(String filePath) {
        String base64Video = null;
        try {
            // Lee el archivo de video
            FileInputStream fileInputStream = new FileInputStream(filePath);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Convierte el video a Base64
            byte[] videoBytes = byteArrayOutputStream.toByteArray();
            base64Video = Base64.encodeToString(videoBytes, Base64.DEFAULT);

            // Cierra los flujos de entrada
            bufferedInputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Video;
    }
}
