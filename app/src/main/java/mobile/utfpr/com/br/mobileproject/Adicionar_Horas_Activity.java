package mobile.utfpr.com.br.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import mobile.utfpr.com.br.mobileproject.model.HoraExtra;
import mobile.utfpr.com.br.mobileproject.persistencia.ConexaoBD;

public class Adicionar_Horas_Activity extends AppCompatActivity {
    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;
    private int    modo;
    private HoraExtra horaExtra;
    private String data;

    ConstraintLayout layout;
    TextView textViewData;
    EditText editTextHora, editTextHoraMin;
    RadioGroup grupo;
    RadioButton tipo50, tipo100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar__horas_);

        layout = (ConstraintLayout)findViewById(R.id.layoutAdicionar);
        layout.setBackgroundColor(PrincipalActivity.opcaoTema);

        editTextHora = (EditText)findViewById(R.id.editTextHora);
        editTextHoraMin = (EditText)findViewById(R.id.editTextMinutos);
        grupo = (RadioGroup)findViewById(R.id.RadioGroup);
        tipo50 = (RadioButton)findViewById(R.id.radioButton50);
        tipo100 = (RadioButton)findViewById(R.id.radioButton100);
        if(horaExtra == null){
            tipo50.setChecked(true);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        textViewData = (TextView) findViewById(R.id.textViewData);


        textViewData.setText(this.data);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        modo = bundle.getInt(PrincipalActivity.MODO);

        if (modo == PrincipalActivity.ALTERAR) {

            long id = bundle.getLong(PrincipalActivity.ID);
            ConexaoBD conexaoBD = ConexaoBD.getInstance(this);
            horaExtra = conexaoBD.horaExtraDAO.horaExtraPorId(id);

            if(horaExtra.getHora50() == null){
                tipo100.setChecked(true);
                String[] hora = horaExtra.getHora100().split(":");
                editTextHora.setText(hora[0]);
                editTextHoraMin.setText(hora[1]);
            }
            else {
                tipo50.setChecked(true);
                String[] hora = horaExtra.getHora50().split(":");
                editTextHora.setText(hora[0]);
                editTextHoraMin.setText(hora[1]);
            }


            setTitle(R.string.alterar_hora);

        }else{

            setTitle(R.string.add_horaExtra);
        }

    }


    private void salvar(){
        String hora50 = null;
        String hora100 = null;

        String hora = editTextHora.getText().toString();
        String minutos = editTextHoraMin.getText().toString();
        if(tipo50.isChecked()){
            hora50 = hora + ":" + minutos;
        }
        if(tipo100.isChecked()) {
            hora100 = hora + ":" + minutos;
        }


        ConexaoBD conexaoBD = ConexaoBD.getInstance(this);

        if (modo == PrincipalActivity.NOVO){

            HoraExtra horaExtra = new HoraExtra(this.data, hora50, hora100);

            conexaoBD.horaExtraDAO.inserir(horaExtra);
            Toast.makeText(this, R.string.salvo, Toast.LENGTH_SHORT).show();

        }else{

            horaExtra.setHora50(hora50);
            horaExtra.setHora100(hora100);

            conexaoBD.horaExtraDAO.alterar(horaExtra);
            Toast.makeText(this, R.string.salvo, Toast.LENGTH_SHORT).show();
        }

        setResult(Activity.RESULT_OK);
        finish();

    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.menuItemCancelar:
                cancelar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
