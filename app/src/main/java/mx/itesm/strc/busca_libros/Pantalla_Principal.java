package mx.itesm.strc.busca_libros;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SearchView;

import java.net.URL;

public class Pantalla_Principal extends AppCompatActivity {

    private EditText isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla__principal);
        isbn = findViewById(R.id.busqueda);
    }

    public void iniciarBusqueda(View v){
        String input = isbn.getText().toString();
        Resultados res = new Resultados();
        res.setIsbn(input);
        FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
        transaccion.addToBackStack(null);
        transaccion.replace(R.id.fragment, res);
        transaccion.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        transaccion.commit();
    }
}



