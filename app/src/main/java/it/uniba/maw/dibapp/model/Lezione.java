package it.uniba.maw.dibapp.model;


import android.util.Log;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;



public class Lezione implements Serializable {

    //costanti per identificare lo stato della lezione
    public static final int LEZIONE_NON_INIZIATA = 0;
    public static final int LEZIONE_IN_REGISTRAZIONE = 1;
    public static final int LEZIONE_TERMINATA = 2;

    //costanti per identificare i campi della lezione
    public static final String ARGOMENTO = "argomento";
    public static final String INSEGNAMENTO = "insegnamento";
    public static final String PROFESSORE = "professore";
    public static final String ORA_INIZIO = "oraInizio";
    public static final String ORA_FINE = "oraFine";
    public static final String STATO = "stato";
    public static final String NAME_SERVER_BLE = "nameServerBle";
    public static final String NUM_PRESENZE = "numPresenze";

    private int numPresenze;
    private String professore;
    private String professoreLink;
    private long data;
    private String oraInizio, oraFine;
    private String argomento;
    private String insegnamento;
    private String insegnamentoLink;
    private String nameServerBle;
    private String linkLezione;
    private ArrayList<String> utentiRegistrati;
    private int stato;

    public Lezione(int numPresenze, String professore, String professoreLink, GregorianCalendar data, String oraInizio, String oraFine, String argomento, String insegnamento, String insegnamentoLink, String nameServerBle) {
        this.numPresenze = numPresenze;
        this.professore = professore;
        this.professoreLink = professoreLink;
        this.data = data.getTimeInMillis();
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.argomento = argomento;
        this.insegnamento = insegnamento;
        this.insegnamentoLink = insegnamentoLink;
        this.nameServerBle = nameServerBle;
        this.stato = LEZIONE_NON_INIZIATA;
        this.utentiRegistrati = null;
    }


    public Lezione() {
    }


    public ArrayList<String> getUtentiRegistrati() {
        return utentiRegistrati;
    }

    public void setUtentiRegistrati(ArrayList<String> utentiRegistrati) {
        this.utentiRegistrati = utentiRegistrati;
    }

    public int getStato() {
        return stato;
    }

    public void setStato(int stato) {
        this.stato = stato;
    }

    public int getNumPresenze() {
        return numPresenze;
    }

    public void setNumPresenze(int numPresenze) {
        this.numPresenze = numPresenze;
    }

    public long getData(){
        return this.data;
    }

    @Exclude
    public GregorianCalendar getGregorianData(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(data);
        return calendar;
    }

    public void setData(long data){
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

    public String getProfessore() {
        return professore;
    }

    public void setProfessore(String professore) {
        this.professore = professore;
    }

    public String getProfessoreLink() {
        return professoreLink;
    }

    public void setProfessoreLink(String professoreLink) {
        this.professoreLink = professoreLink;
    }

    public String getInsegnamentoLink() {
        return insegnamentoLink;
    }

    public void setInsegnamentoLink(String insegnamentoLink) { this.insegnamentoLink = insegnamentoLink; }

    public String getNameServerBle() { return nameServerBle; }

    @Exclude
    public String getLinkLezione() {
        return linkLezione;
    }

    @Exclude
    public void setLinkLezione(String linkLezione) {
        this.linkLezione = linkLezione;
    }

    public void setNameServerBle(String nameServerBle) { this.nameServerBle = nameServerBle; }

    @Override
    public String toString() {
        return "Lezione{" +
                "numPresenze=" + numPresenze +
                ", professore='" + professore + '\'' +
                ", professoreLink='" + professoreLink + '\'' +
                ", data=" + data +
                ", oraInizio='" + oraInizio + '\'' +
                ", oraFine='" + oraFine + '\'' +
                ", argomento='" + argomento + '\'' +
                ", insegnamento='" + insegnamento + '\'' +
                ", insegnamentoLink='" + insegnamentoLink + '\'' +
                '}';
    }
}


