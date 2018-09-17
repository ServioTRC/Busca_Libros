package mx.itesm.strc.busca_libros;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

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
        if(Lectura_Web.validar_existencia(input)){
            Resultados res = new Resultados();
            FragmentTransaction transaccion = getSupportFragmentManager().beginTransaction();
            transaccion.replace(R.id.fragment, res);
            transaccion.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
            transaccion.addToBackStack(null);
            transaccion.commit();
        } else{
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setTitle("Aviso")
                    .setMessage("ISBN no encontrado")
                    .setPositiveButton("Aceptar", null)
                    .show();
        }
    }
}
