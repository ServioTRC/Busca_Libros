package mx.itesm.strc.busca_libros;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Lectura_Web {

    public static String titulo;
    public static String autores;
    public static String descripcion;
    public static Bitmap portada;
    public static boolean response;

    public Lectura_Web(){

    }

    public static boolean validar_existencia(String isbn){
        Lectura_Web.response = true;
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray arrItems = response.getJSONArray("items");
                            JSONObject  item = arrItems.getJSONObject(0);
                            JSONObject info = item.getJSONObject("volumeInfo");
                            Lectura_Web.titulo = "\t" + info.getString("title");

                            JSONArray autores = info.getJSONArray("authors");
                            String autores_res = "\t";
                            for(int i = 0; i < autores.length(); i++){
                                autores_res += ", ";
                                autores_res += autores.getString(i);
                            }
                            Lectura_Web.autores = autores_res;

                            Lectura_Web.descripcion = info.getString("description");

                            JSONObject imagenLink = info.getJSONObject("imageLinks");
                            String imagen = imagenLink.getString("thumbnail");


                            Lectura_Web.descargarImagenLibro(imagen);

                        } catch (JSONException e) {
                            Log.i("Lectura web", e.toString());
                            Lectura_Web.response = false;
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Lectura_Web.response = false;
                    }
                });
        return Lectura_Web.response;
    }

    private static void descargarImagenLibro(String imagen){
        imagen = imagen.replace("http:", "https:");
        AndroidNetworking.get(imagen.toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Lectura_Web.portada = response;
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

}
