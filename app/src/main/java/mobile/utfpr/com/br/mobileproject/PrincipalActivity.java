package mobile.utfpr.com.br.mobileproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import mobile.utfpr.com.br.mobileproject.model.Config;
import mobile.utfpr.com.br.mobileproject.model.HoraExtra;
import mobile.utfpr.com.br.mobileproject.persistencia.ConexaoBD;
import mobile.utfpr.com.br.mobileproject.utilitario.Processamento;
import mobile.utfpr.com.br.mobileproject.utilitario.Utils;

public class PrincipalActivity extends AppCompatActivity {

    private TextView textViewTotalHoras, textViewTotalBanco;
    private CalendarView calendario;
    public static int opcaoTema = Color.WHITE;
    private ListView listViewHorasExtras;
    private ArrayAdapter<HoraExtra> listaAdapter;
    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private ActionMode actionMode;
    private int        posicaoSelecionada = -1;
    private View viewSelecionada;

    private ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        layout = (ConstraintLayout)findViewById(R.id.layoutPrincipal);
        textViewTotalHoras= (TextView)findViewById(R.id.textViewTotalHoras);
        textViewTotalBanco= (TextView)findViewById(R.id.textViewTotalBanco);
        calendario = (CalendarView)findViewById(R.id.calendarViewCalendario);
        listViewHorasExtras = (ListView)findViewById(R.id.ListViewHorasExtras);


        /*
        listViewHorasExtras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HoraExtra horaExtra = (HoraExtra) parent.getItemAtPosition(position);

            }
        });*/


        popularListaHoras();
        processarDados();
        registerForContextMenu(listViewHorasExtras);


        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                calendario = calendarView;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == NOVO || requestCode == ALTERAR) &&
                resultCode == Activity.RESULT_OK){

            listaAdapter.notifyDataSetChanged();
            processarDados();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.menu_item_select, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        HoraExtra horaExtra =
                (HoraExtra) listViewHorasExtras.getItemAtPosition(info.position);

        switch (item.getItemId()) {

            case R.id.itemSelectEditar:
                Intent intent = new Intent(PrincipalActivity.this,
                        Adicionar_Horas_Activity.class);
                intent.putExtra("dataLongMiliseconds",
                        (Long) calendario.getDate());
                intent.putExtra(MODO, ALTERAR);
                intent.putExtra(ID, horaExtra.getId());
                startActivityForResult(intent, ALTERAR);
                return true;

            case R.id.itemSelectExcluir:
                String mensagem = getString(R.string.deseja_realmente_apagar)
                        + "\n" + horaExtra.getData();

                final HoraExtra horaExtraExclusao = horaExtra;

                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:

                                        listaAdapter.remove(horaExtraExclusao);

                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };

                Utils.confirmaAcao(this, mensagem, listener);
                return true;
            case R.id.itemSelectMarcar:
                Intent intentTag = new Intent(this, Activity_VerHoras.class);
                startActivity(intentTag);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuItemAdd:
                final Date data = null;

                Intent intent = new Intent(PrincipalActivity.this,
                        Adicionar_Horas_Activity.class);
                intent.putExtra("dataLongMiliseconds",
                        (Long) calendario.getDate());
                intent.putExtra(MODO, NOVO);
                startActivityForResult(intent, NOVO);
                return true;
            case R.id.menuItemConfig:
                ConexaoBD conexaoBD = ConexaoBD.getInstance(this);
                Config config = conexaoBD.configDAO.carregaConfig();

                Intent intentConfig = new Intent(PrincipalActivity.this,
                        ConfigActivity.class);
                intentConfig.putExtra(MODO, ALTERAR);
                intentConfig.putExtra(ID, config.getId());
                startActivityForResult(intentConfig, ALTERAR);
                return true;
            case R.id.menuItemSobre:
                Intent intentAbout = new Intent(this, ActivityAbout.class);
                startActivity(intentAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void popularListaHoras(){
        ConexaoBD conexao = ConexaoBD.getInstance(this);

        listaAdapter = new ArrayAdapter<HoraExtra>(this,
                android.R.layout.simple_list_item_1,
                conexao.horaExtraDAO.lista);

        listViewHorasExtras.setAdapter(listaAdapter);
    }





    private void getTema(){
        SharedPreferences sharedPref =
                getSharedPreferences(getString(R.string.prefencias_cores),
                        Context.MODE_PRIVATE);

        opcaoTema = sharedPref.getInt(getString(R.string.cor_fundo), opcaoTema);

        mudarCorFundo();
    }

    private void mudarCorFundo(){
        layout.setBackgroundColor(opcaoTema);
    }


    private void processarDados(){
        Config config = null;
        ConexaoBD conexaoBD = ConexaoBD.getInstance(this);
        Processamento processamento =
                new Processamento(conexaoBD.configDAO.carregaConfig(),
                        conexaoBD.horaExtraDAO.listaHorasExtras());
        config = processamento.processaDados();
        Toast.makeText(this, ""+config.getVarlorDeHoras(), Toast.LENGTH_SHORT).show();
        textViewTotalBanco.setText(config.getBanco());
        textViewTotalHoras.setText(""+config.getVarlorDeHoras());

    }


}
