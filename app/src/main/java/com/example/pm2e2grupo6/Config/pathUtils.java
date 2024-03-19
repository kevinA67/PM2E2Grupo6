package com.example.pm2e2grupo6.Config;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
public class pathUtils {
    // Método para obtener la ruta real de una URI
    public static String getRealPathFromURI(Context context, Uri uri) {
        String realPath = null;
        // Si la URI es de tipo 'file://', simplemente obtenemos el path de la URI
        if ("file".equals(uri.getScheme())) {
            realPath = uri.getPath();
        } else {
            // Si no es de tipo 'file://', intentamos obtener el path desde el proveedor de contenido
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    if (index != -1) {
                        realPath = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return realPath;
    }
}
