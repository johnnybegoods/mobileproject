package mobile.utfpr.com.br.mobileproject.utilitario;

import java.text.DecimalFormat;
import java.util.ArrayList;

import mobile.utfpr.com.br.mobileproject.model.Config;
import mobile.utfpr.com.br.mobileproject.model.HoraExtra;

/**
 * Created by johnny on 21/11/17.
 */

public class Processamento {
    private int minuto;
    private int hora50 = 0;
    private int hora100 = 0;
    private int minuto50 = 0;
    private int minuto100 = 0;
    private Config config;
    ArrayList<HoraExtra> horasExtras;

    public Processamento(Config config, ArrayList<HoraExtra> horasExtras){
        this.config = config;
        this.horasExtras = horasExtras;
    }

    public Config processaDados(){


        String bancoDeHoras = processaHorasBanco(horasExtras);
        Double valor = processaValor(horasExtras);

        this.config.setBanco(bancoDeHoras);
        this.config.setVarlorDeHoras(valor);
        return this.config;

    }



    private Double processaValor(ArrayList<HoraExtra> horasExtras){
        for (int i = 0; i < horasExtras.size(); i++){
            if (horasExtras.get(i).getHora50() != null){
                somaValor(horasExtras.get(i).getHora50(),1);
            }
            if (horasExtras.get(i).getHora100() != null){
                somaValor(horasExtras.get(i).getHora100(), 2);
            }
        }

        Double vlr50 = this.config.getSalario() / this.config.getCarga_horaria();
        vlr50 = vlr50 + (vlr50/2);
        Double vlr100 = this.config.getSalario() / this.config.getCarga_horaria();
        vlr100 = vlr100 * 2;

        Double valorHora50 = vlr50 * this.hora50;
        Double valor100 = vlr100 * this.hora100;

        return valorHora50 + valor100;

    }



    private String processaHorasBanco(ArrayList<HoraExtra> horasExtras){
        String add;

        for (int i = 0; i < horasExtras.size(); i++){
            if (horasExtras.get(i).getHora50() != null){
                Adiciona50(horasExtras.get(i).getHora50());
            }
            if (horasExtras.get(i).getHora100() != null){
                Adiciona100(horasExtras.get(i).getHora100());
            }
        }
        return getHoras(this.minuto50 + this.minuto100);
    }

    private void Adiciona50(String horas) {
        int minuto = 0;

        String[] hrs = horas.split(":");
        minuto = (Integer.parseInt(hrs[0]) * 60)
                + (Integer.parseInt(hrs[1]));
        minuto += (Integer.parseInt(hrs[0]) * 60)/2 + (Integer.parseInt(hrs[1])/2);
        this.minuto50 += minuto;
    }

    private void Adiciona100(String horas) {
        int minuto = 0;
        String[] hrs = horas.split(":");
        minuto += (Integer.parseInt(hrs[0]) * 60) + (Integer.parseInt(hrs[1]));
        this.minuto100 += minuto;
    }



    public void somaValor(String horas, int tipo) {
        String[] hrs = horas.split(":");
        if(tipo == 1){
            this.hora50 += (Integer.parseInt(hrs[0]) * 60) + (Integer.parseInt(hrs[1]));
        }else{
            this.hora100 += (Integer.parseInt(hrs[0]) * 60) + (Integer.parseInt(hrs[1]));
        }
    }


    private String getHoras(int minuto) {
        String horas;
        DecimalFormat fmt = new DecimalFormat("00");
        horas = Integer.toString(minuto / 60);

        horas += ":";
        horas += fmt.format(minuto % 60);
        if(horas.length() == 4){
            horas = "0"+horas;
        }

        if(horas.indexOf("-") > 0){
            String texto = "";
            int regex = horas.indexOf("-");
            texto = horas.substring(0, regex);
            texto = texto+horas.substring(regex+1);
            if(texto.length() == 4){
                texto = "0"+texto;
            }
            return "-"+texto;
        }
        else{
            return horas;
        }
    }


}
