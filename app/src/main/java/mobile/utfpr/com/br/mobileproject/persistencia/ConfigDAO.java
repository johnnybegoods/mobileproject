package mobile.utfpr.com.br.mobileproject.persistencia;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mobile.utfpr.com.br.mobileproject.model.Config;

/**
 * Created by johnny on 21/11/17.
 */

public class ConfigDAO {
    private ConexaoBD conexao;

    public static final String TABELA = "CONFIGURACOES";
    public static final String ID     = "ID";
    public static final String SALARIO     = "SALARIO";
    public static final String CARGA_HORARIA   = "CARGA_HORARIA";
    public static final String BANCO_DE_HORAS  = "BANCO_DE_HORAS";
    public static final String VALOR_DE_HORAS  = "VALOR_DE_HORAS";

    public Config config;

    public ConfigDAO(ConexaoBD conexaoDatabase){
        conexao = conexaoDatabase;
    }

    public void criarTabela(SQLiteDatabase database){

        String sql = "CREATE TABLE " + TABELA + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                SALARIO + " DOUBLE NOT NULL, " +
                CARGA_HORARIA  + " INTEGER, " +
                BANCO_DE_HORAS + " TEXT, "+
                VALOR_DE_HORAS + " DOUBLE)";

        database.execSQL(sql);
    }

    public void apagarTabela(SQLiteDatabase database){

        String sql = "DROP TABLE IF EXISTS " + TABELA;

        database.execSQL(sql);

    }

    public boolean inserir(Config config){

        ContentValues values = new ContentValues();

        values.put(SALARIO, config.getSalario());
        values.put(CARGA_HORARIA, config.getCarga_horaria());
        values.put(BANCO_DE_HORAS, config.getBanco());
        values.put(VALOR_DE_HORAS, config.getVarlorDeHoras());


        long id = conexao.getWritableDatabase().insert(TABELA,
                null,
                values);
        config.setId(id);

        return true;
    }

    public void inicializarConfig(){
        Config config = new Config(0, 0);

        ContentValues values = new ContentValues();

        values.put(SALARIO, config.getSalario());
        values.put(CARGA_HORARIA, config.getCarga_horaria());
        values.put(BANCO_DE_HORAS, config.getBanco());
        values.put(VALOR_DE_HORAS, config.getVarlorDeHoras());


        long id = conexao.getWritableDatabase().insert(TABELA,
                null,
                values);
    }



    public boolean alterar(Config config){

        ContentValues values = new ContentValues();


        values.put(SALARIO, config.getSalario());
        values.put(CARGA_HORARIA, config.getCarga_horaria());

        String[] args = {String.valueOf(config.getId())};

        conexao.getWritableDatabase().update(TABELA,
                values,
                ID + " = ?",
                args);

        return true;
    }



    public Config carregaConfig(){


        String sql = "SELECT * FROM " + TABELA;

        Cursor cursor = conexao.getReadableDatabase().rawQuery(sql, null);

        int colunaSal    = cursor.getColumnIndex(SALARIO);
        int colunaCH  = cursor.getColumnIndex(CARGA_HORARIA);
        int colunaBanco = cursor.getColumnIndex(BANCO_DE_HORAS);
        int colunaValor = cursor.getColumnIndex(VALOR_DE_HORAS);
        int colunaId = cursor.getColumnIndex(ID);

        //Config configuracoes = null;

        while(cursor.moveToNext()){

            Config configuracoes = new Config(
                    cursor.getDouble(colunaSal), cursor.getInt(colunaCH));

            configuracoes.setBanco(cursor.getString(colunaBanco));
            configuracoes.setVarlorDeHoras(cursor.getDouble(colunaValor));
            configuracoes.setId(cursor.getLong(colunaId));
            this.config = configuracoes;
        }

        if(this.config == null){
            this.config = new Config(0,0);
        }

        cursor.close();
        return this.config;
    }


}
