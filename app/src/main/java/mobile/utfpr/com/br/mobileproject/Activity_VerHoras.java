package mobile.utfpr.com.br.mobileproject;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.utfpr.com.br.mobileproject.model.HoraExtra;
import mobile.utfpr.com.br.mobileproject.persistencia.ConexaoBD;

public class Activity_VerHoras extends AppCompatActivity {
    private ListView listViewHoras;
    private ArrayAdapter<HoraExtra> listaAdapter;

    private ArrayList<HoraExtra> listaHoras;


    private int posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__ver_horas);

        listViewHoras = (ListView) findViewById(R.id.listViewVerHoras);


        listViewHoras.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listViewHoras.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {


            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

                boolean selecionado = listViewHoras.isItemChecked(position);

                View view = listViewHoras.getChildAt(position);

                if (selecionado){
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                int totalSelecionados = listViewHoras.getCheckedItemCount();

                if (totalSelecionados > 0){

                    mode.setTitle(getResources().getQuantityString(R.plurals.selecionado,
                            totalSelecionados,
                            totalSelecionados));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_ver_horas, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

                return false;
            }


            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {

                switch(item.getItemId()){
                    case R.id.marcarHora:
                        marcarHorasPagas();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

                for (int posicao = 0; posicao < listViewHoras.getChildCount(); posicao++){

                    View view = listViewHoras.getChildAt(posicao);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        popularLista();
        setTitle(R.string.marcar_horas_pagas);
    }

    public void marcarHorasPagas(){
       /*
        ConexaoBD conexaoBD = ConexaoBD.getInstance(this);
        listaHoras = conexaoBD.horaExtraDAO.listaHorasExtras();

        for (int posicao = listViewHoras.getChildCount(); posicao >= 0; posicao--){

            if (listViewHoras.isItemChecked(posicao)){
                listaHoras.get(posicao).setPaga(true);
                conexaoBD.horaExtraDAO.alterar(listaHoras.get(posicao));
            }
        }
        popularLista();
        // listaAdapter.setNotifyOnChange(true); */
        Toast.makeText(this, R.string.erro_projeto, Toast.LENGTH_SHORT).show();
    }

    private void popularLista(){
        ConexaoBD conexao = ConexaoBD.getInstance(this);
        listaHoras = new ArrayList<HoraExtra>();
        listaHoras = conexao.horaExtraDAO.listaHorasExtras();

        listaAdapter = new ArrayAdapter<HoraExtra>(this,
                android.R.layout.simple_list_item_1,
                conexao.horaExtraDAO.lista);

        listViewHoras.setAdapter(listaAdapter);
    }
}
