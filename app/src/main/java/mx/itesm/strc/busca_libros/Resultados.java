package mx.itesm.strc.busca_libros;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Resultados extends Fragment {

    private String isbn;
    private TextView titulo;
    private TextView autores;
    private TextView descripcion;
    private TextView isbn_display;
    private ImageView portada;
    private ImageView libro_no;
    private TextView coincidencias;
    private LinearLayout datos;
    private LinearLayout no_encontrado;



    public Resultados() {
        // Required empty public constructor
    }

    public void setIsbn(String isbn){
        this.isbn = isbn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_resultados, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View v = getView();
        if( v != null){
            titulo = v.findViewById(R.id.titulo);
            autores = v.findViewById(R.id.autores);
            descripcion = v.findViewById(R.id.descripcion);
            coincidencias = v.findViewById(R.id.coincidencias);
            datos = v.findViewById(R.id.datos);
            portada = v.findViewById(R.id.imagen_portada);
            isbn_display = v.findViewById(R.id.isbn);
            libro_no = v.findViewById(R.id.libro_no);
            no_encontrado = v.findViewById(R.id.error);

            datos.setVisibility(View.GONE);
            coincidencias.setVisibility(View.GONE);
            no_encontrado.setVisibility(View.GONE);
            String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
            new TareaDescarga().execute(url);
        }
    }

    private class TareaDescarga extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected String doInBackground(String... urls) {
            res = "Correcto";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject response = new JSONObject(builder.toString());

                JSONArray arrItems = response.getJSONArray("items");
                JSONObject  item = arrItems.getJSONObject(0);
                JSONObject info = item.getJSONObject("volumeInfo");
                titulo.setText("\t" + info.getString("title"));
                Log.i("Bien", titulo.getText().toString());

                JSONArray autores_json = info.getJSONArray("authors");
                String autores_res = "\t";
                for(int i = 0; i < autores_json.length(); i++){
                    autores_res += autores_json.getString(i);
                    if(i != autores_json.length() - 1)
                        autores_res += ", ";
                }
                autores.setText(autores_res);
                Log.i("Bien", autores_res);

                descripcion.setText(info.getString("description"));
                Log.i("Bien", descripcion.getText().toString());

                JSONObject imagenLink = info.getJSONObject("imageLinks");
                String imagen = imagenLink.getString("thumbnail");
                descargarImagenLibro(imagen);

                isbn_display.setText("\t" + isbn);

            } catch (Exception e) {
                Log.i("Lectura web", e.toString());
                res = "Error Json";
            }
            Log.i("Async", res);
            return res;
        }

        private void descargarImagenLibro(String imagen){
            imagen = imagen.replace("http:", "https:");
            AndroidNetworking.get(imagen.toString())
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsBitmap(new BitmapRequestListener() {
                        @Override
                        public void onResponse(Bitmap response) {
                            portada.setImageBitmap(response);
                            Log.i("Imagen", "Bien");
                        }

                        @Override
                        public void onError(ANError anError) {
                            Log.i("Lectura Imagen", anError.toString());
                        }
                    });
        }

        @Override
        protected void onPreExecute() {
            coincidencias.setText("Buscando\ncoincidencias\nde ISBN");
            libro_no.setVisibility(View.GONE);
            coincidencias.setVisibility(View.VISIBLE);
            no_encontrado.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... voids) {}

        @Override
        protected void onPostExecute(String s) {
            if(res.equals("Correcto")){
                no_encontrado.setVisibility(View.GONE);
                datos.setVisibility(View.VISIBLE);
            } else {
                no_encontrado.setVisibility(View.VISIBLE);
                libro_no.setVisibility(View.VISIBLE);
                coincidencias.setText("ISBN no\n encontrado\n("+isbn+")\n\nVuelva a\ningresar su\nbúsqueda");
            }
        }
    }

}

