package mobile.utfpr.com.br.mobileproject.model;

/**
 * Created by johnny on 21/11/17.
 */

public class Config {
    private long id;
    private double salario;
    private int carga_horaria;
    private String banco;
    private double varlorDeHoras;

    public Config(double salario, int carga_horaria) {
        this.salario = salario;
        this.carga_horaria = carga_horaria;
        this.banco = "00:00";
        this.varlorDeHoras = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public int getCarga_horaria() {
        return carga_horaria;
    }

    public void setCarga_horaria(int carga_horaria) {
        this.carga_horaria = carga_horaria;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public double getVarlorDeHoras() {
        return varlorDeHoras;
    }

    public void setVarlorDeHoras(double varlorDeHoras) {
        this.varlorDeHoras = varlorDeHoras;
    }
}
