package it.uniba.maw.dibapp.model;

import java.time.LocalTime;
import java.util.Date;

public class Valutazione {

    private int id, voto, lezione;
    private String commento;
    private boolean visibilita;
    private Date data;
    private LocalTime ora;

    public Valutazione(int id, int voto, int lezione, String commento, boolean visibilita, Date data, LocalTime ora) {
        this.id = id;
        this.voto = voto;
        this.lezione = lezione;
        this.commento = commento;
        this.visibilita = visibilita;
        this.data = data;
        this.ora = ora;
    }

    public Valutazione() {

    }
}




