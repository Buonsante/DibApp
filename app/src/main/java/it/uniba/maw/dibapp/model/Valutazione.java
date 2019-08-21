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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public int getLezione() {
        return lezione;
    }

    public void setLezione(int lezione) {
        this.lezione = lezione;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public boolean isVisibilita() {
        return visibilita;
    }

    public void setVisibilita(boolean visibilita) {
        this.visibilita = visibilita;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public LocalTime getOra() {
        return ora;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }
}




