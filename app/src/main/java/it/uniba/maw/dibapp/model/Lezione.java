package it.uniba.maw.dibapp.model;


import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Lezione {

    private int numPresenze;
    private GregorianCalendar data;
    private String oraInizio, oraFine;
    private String argomento;
    private String insegnamento;

    public Lezione(int numPresenze, GregorianCalendar data, String oraInizio, String oraFine, String argomento, String insegnamento) {
        this.numPresenze = numPresenze;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.argomento = argomento;
        this.insegnamento = insegnamento;
    }

    public Lezione() {
    }

    public int getNumPresenze() {
        return numPresenze;
    }

    public void setNumPresenze(int numPresenze) {
        this.numPresenze = numPresenze;
    }

    public GregorianCalendar getData() {
        return data;
    }

    public void setData(GregorianCalendar data) {
        this.data = data;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getOraFine() {
        return oraFine;
    }

    public void setOraFine(String oraFine) {
        this.oraFine = oraFine;
    }

    public String getArgomento() {
        return argomento;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }
    public void setInsegnamento(String insegnamento) {
        this.insegnamento = insegnamento;
    }

    public String getInsegnamento() {
        return insegnamento;
    }


    public List<Lezione> getLezioniProva() {

        List<Lezione> lezioni = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            lezioni.add(new Lezione(0, calendar, "10:00", "12:00",
                                        "argomento", "insegnamento" + (i+1)));
        }


        return lezioni;

    }


}


