package mobile.utfpr.com.br.mobileproject.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobile.utfpr.com.br.mobileproject.model.HoraExtra;

/**
 * Created by johnny on 21/11/17.
 */

public class HoraExtraDAO {
    public static final String TABELA = "HORA_EXTRA";
    public static final String ID     = "ID";
    public static final String DATA   = "DATA";
    public static final String HORA50  = "HORA50";
    public static final String HORA100  = "HORA100";
    public static final String PAGA  = "PAGA";

    private ConexaoBD conexao;
    public List<HoraExtra> lista;

    public HoraExtraDAO(ConexaoBD conexaoDatabase){
        conexao = conexaoDatabase;
        lista   = new ArrayList<HoraExtra>();
    }

    public void criarTabela(SQLiteDatabase database){

        String sql = "CREATE TABLE " + TABELA + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                DATA  + " TEXT NOT NULL, " +
                HORA50 + " TEXT, "+
                HORA100 + " TEXT, " +
                PAGA + " INTEGER)";

        database.execSQL(sql);
    }

    public void apagarTabela(SQLiteDatabase database){

        String sql = "DROP TABLE IF EXISTS " + TABELA;

        database.execSQL(sql);
    }

    public boolean inserir(HoraExtra horaExtra){

        ContentValues values = new ContentValues();

        values.put(DATA, horaExtra.getData());
        values.put(HORA50, horaExtra.getHora50());
        values.put(HORA100, horaExtra.getHora100());


        long id = conexao.getWritableDatabase().insert(TABELA,
                null,
                values);

        horaExtra.setId(id);

        lista.add(horaExtra);

        return true;
    }

    public boolean alterar(HoraExtra horaExtra){

        ContentValues values = new ContentValues();


        values.put(HORA50, horaExtra.getHora50());
        values.put(HORA100, horaExtra.getHora100());

        String[] args = {String.valueOf(horaExtra.getId())};

        conexao.getWritableDatabase().update(TABELA,
                values,
                ID + " = ?",
                args);

        ordenarLista();

        return true;
    }

    public boolean apagar(HoraExtra horaExtra){

        String[] args = {String.valueOf(horaExtra.getId())};

        conexao.getWritableDatabase().delete(TABELA,
                ID + " = ?",
                args);
        lista.remove(horaExtra);

        return true;
    }

    public void carregarTudo(){

        lista.clear();

        String sql = "SELECT * FROM " + TABELA + " ORDER BY " + DATA;

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int colunaId    = cursor.getColumnIndex(ID);
        int colunaData  = cursor.getColumnIndex(DATA);
        int colunaHora50 = cursor.getColumnIndex(HORA50);
        int colunaHora100 = cursor.getColumnIndex(HORA100);
        int colunaPaga = cursor.getColumnIndex(PAGA);

        while(cursor.moveToNext()){

            HoraExtra horaExtra = new HoraExtra(cursor.getString(colunaData),
                    cursor.getString(colunaHora50), cursor.getString(colunaHora100));

            horaExtra.setId(cursor.getLong(colunaId));
            if(cursor.getInt(colunaPaga) == 0){
                horaExtra.setPaga(true);
            }
            else {
                horaExtra.setPaga(false);
            }

            lista.add(horaExtra);
        }

        cursor.close();
    }

    public HoraExtra horaExtraPorId(long id){

        for (HoraExtra horaExtra: lista){

            if (horaExtra.getId() == id){
                return horaExtra;
            }
        }

        return null;
    }

    public void ordenarLista(){
        Collections.sort(lista, HoraExtra.comparador);
    }

    public ArrayList<HoraExtra> listaHorasExtras(){

        ArrayList<HoraExtra> listaHorasExtras = new ArrayList<HoraExtra>();

        String sql = "SELECT * FROM " + TABELA + " ORDER BY " + DATA;

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int colunaId    = cursor.getColumnIndex(ID);
        int colunaData  = cursor.getColumnIndex(DATA);
        int colunaHora50 = cursor.getColumnIndex(HORA50);
        int colunaHora100 = cursor.getColumnIndex(HORA100);
        int colunaPaga = cursor.getColumnIndex(PAGA);

        while(cursor.moveToNext()){

            HoraExtra horaExtra = new HoraExtra(cursor.getString(colunaData),
                    cursor.getString(colunaHora50), cursor.getString(colunaHora100));

            horaExtra.setId(cursor.getLong(colunaId));
            if(cursor.getInt(colunaPaga) == 0){
                horaExtra.setPaga(true);
            }
            else {
                horaExtra.setPaga(false);
            }

            if(!horaExtra.isPaga()){
                listaHorasExtras.add(horaExtra);
            }
        }

        cursor.close();
        return listaHorasExtras;
    }

}
