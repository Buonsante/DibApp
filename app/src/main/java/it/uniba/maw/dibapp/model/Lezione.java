package it.uniba.maw.dibapp.model;

import java.util.Date;
import java.time.LocalTime;

public class Lezione {

    private int numPresenze;
    private Date data;
    private LocalTime oraInizio, oraFine;
    private String argomento;

    public Lezione(int numPresenze, Date data, LocalTime oraInizio, LocalTime oraFine, String argomento) {
        this.numPresenze = numPresenze;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.argomento = argomento;
    }

    public Lezione() {
    }

    public int getNumPresenze() {
        return numPresenze;
    }

    public void setNumPresenze(int numPresenze) {
        this.numPresenze = numPresenze;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LocalTime getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(LocalTime oraInizio) {
        this.oraInizio = oraInizio;
    }

    public LocalTime getOraFine() {
        return oraFine;
    }

    public void setOraFine(LocalTime oraFine) {
        this.oraFine = oraFine;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }
}


