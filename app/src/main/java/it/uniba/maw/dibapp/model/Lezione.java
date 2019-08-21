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


}


