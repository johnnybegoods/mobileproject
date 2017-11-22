package mobile.utfpr.com.br.mobileproject.model;

import java.util.Comparator;

/**
 * Created by johnny on 21/11/17.
 */

public class HoraExtra {
    public static Comparator<HoraExtra> comparador
            = new Comparator<HoraExtra>() {
        @Override
        public int compare(HoraExtra horaExtra1, HoraExtra horaExtra2) {
            return horaExtra1.getData().compareToIgnoreCase(horaExtra2.getData());
        }
    };

    private long id;
    private String data;
    private String hora50;
    private String hora100;
    private boolean paga;

    public HoraExtra(){}

    public HoraExtra(String data, String hora50, String hora100) {
        this.data = data;
        this.hora50 = hora50;
        this.hora100 = hora100;
        this.paga = false;
    }

    public String toString() {

        if(hora50 == null) {
            if(!paga){
                return "100%:  " + hora100;
            }
            else{
                return "100%:   " + hora100 + "\t\t"+ "Paga";
            }
        }
        else  {
            if(!paga){
                return "50%:  "+hora50;
            }
            else{
                return "50%:  " + hora50 + "\t\t" + "Paga";
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora50() {
        return hora50;
    }

    public void setHora50(String hora50) {
        this.hora50 = hora50;
    }

    public String getHora100() {
        return hora100;
    }

    public void setHora100(String hora100) {
        this.hora100 = hora100;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }
}
