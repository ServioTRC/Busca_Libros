package mx.itesm.strc.busca_libros;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Resultados extends Fragment {

    public Resultados() {
        // Required empty public constructor
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
            TextView titulo = v.findViewById(R.id.titulo);
            TextView autores = v.findViewById(R.id.autores);
            TextView descripcion = v.findViewById(R.id.descripcion);
            titulo.setText(Lectura_Web.titulo);
            autores.setText(Lectura_Web.autores);
            descripcion.setText(Lectura_Web.descripcion);
        }
    }
}
