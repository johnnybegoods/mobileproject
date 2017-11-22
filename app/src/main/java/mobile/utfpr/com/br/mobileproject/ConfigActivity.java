package mobile.utfpr.com.br.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.utfpr.com.br.mobileproject.model.Config;
import mobile.utfpr.com.br.mobileproject.persistencia.ConexaoBD;

public class ConfigActivity extends AppCompatActivity {
    Config config;
    EditText editTextSalario, editTextCH;
    Spinner spinnerTema;
    int opcaoTema = Color.WHITE;
    private ConstraintLayout layout;
    private boolean novo = false;
    private int    modo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        editTextSalario = (EditText)findViewById(R.id.editTextSalario);
        editTextCH = (EditText)findViewById(R.id.editTextCargaHoraria);
        spinnerTema = (Spinner)findViewById(R.id.spinnerTema);
        layout = (ConstraintLayout)findViewById(R.id.layoutConfig);
        layout.setBackgroundColor(PrincipalActivity.opcaoTema);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        modo = bundle.getInt(PrincipalActivity.MODO);

        if (modo == PrincipalActivity.ALTERAR) {

            ConexaoBD conexaoBD = ConexaoBD.getInstance(this);
            config = conexaoBD.configDAO.carregaConfig();
            editTextSalario.setText("" + config.getSalario());
            editTextCH.setText("" + config.getCarga_horaria());
        }

        editTextSalario.setText(""+config.getSalario());
        editTextCH.setText(""+config.getCarga_horaria());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lerPreferenciaTema();
        popularSpinner();
        setTitle(R.string.configuracao);

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

    private void cancelar(){
        finish();
    }

    private void salvar(){
        Double salario = Double.parseDouble(editTextSalario.getText().toString());
        int ch = Integer.parseInt(editTextCH.getText().toString());

        ConexaoBD conexaoBD = ConexaoBD.getInstance(this);

        config.setSalario(salario);
        config.setCarga_horaria(ch);
        conexaoBD.configDAO.alterar(config);
        Toast.makeText(this, R.string.valores_atualizados, Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK);
        finish();

        //Toast.makeText(this, "Implementar Salvar", Toast.LENGTH_SHORT);
    }

    private void popularSpinner(){

        ArrayList<String> lista = new ArrayList<String>();

        lista.add("Simple Office");
        lista.add("Blue Office");
        lista.add("Yellow Office");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        lista);

        spinnerTema.setAdapter(adapter);
    }


    private void lerPreferenciaTema(){
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.prefencias_cores),
                        Context.MODE_PRIVATE);

        opcaoTema = sharedPref.getInt(getString(R.string.cor_fundo), opcaoTema);

        mudarCorFundo();
    }


    private void salvarTema(int novoValor){
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.prefencias_cores),
                        Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.cor_fundo), novoValor);

        editor.commit();

        opcaoTema = novoValor;

        mudarCorFundo();
    }

    private void mudarCorFundo(){
        layout.setBackgroundColor(opcaoTema);
    }

    private void setarTema(){
        String tema = (String)spinnerTema.getSelectedItem();

        if(tema.equals("Simple Office")){
            salvarTema(Color.WHITE);
        }
        if(tema.equals("Blue Office")){
            salvarTema(Color.BLUE);
        }
        if(tema.equals("Yellow Office")){
            salvarTema(Color.YELLOW);
        }
    }


}
