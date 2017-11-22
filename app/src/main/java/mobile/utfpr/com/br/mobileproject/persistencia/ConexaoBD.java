package mobile.utfpr.com.br.mobileproject.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by johnny on 21/11/17.
 */

public class ConexaoBD extends SQLiteOpenHelper {
    private static final String DB_NAME = "obank.db";
    private static final int DB_VERSION = 1;

    private static ConexaoBD instance;

    private Context context;
    public  HoraExtraDAO horaExtraDAO;
    public ConfigDAO configDAO;

    public static ConexaoBD getInstance(Context contexto){
        if (instance == null){
            instance = new ConexaoBD(contexto);
        }

        return instance;
    }

    private ConexaoBD(Context contexto){
        super(contexto, DB_NAME, null, DB_VERSION);

        context = contexto;

        horaExtraDAO = new HoraExtraDAO(this);
        configDAO = new ConfigDAO(this);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        horaExtraDAO.criarTabela(db);
        configDAO.criarTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        horaExtraDAO.apagarTabela(db);
        configDAO.apagarTabela(db);

        onCreate(db);
    }


}
