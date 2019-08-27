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

    private int numPresenze;
    private String professore;
    private String professoreLink;
    private long data;
    private String oraInizio, oraFine;
    private String argomento;
    private String insegnamento;
    private String insegnamentoLink;

    public Lezione(int numPresenze, String professore, String professoreLink, GregorianCalendar data, String oraInizio, String oraFine, String argomento, String insegnamento, String insegnamentoLink) {
        this.numPresenze = numPresenze;
        this.professore = professore;
        this.professoreLink = professoreLink;
        this.data = data.getTimeInMillis();
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.argomento = argomento;
        this.insegnamento = insegnamento;
        this.insegnamentoLink = insegnamentoLink;
    }

    public Lezione() {
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
//    @Exclude
//    public GregorianCalendar getData() {
//        return data;
//    }

    @Exclude
    public GregorianCalendar getGregorianData(){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(data);
        return calendar;
    }

    public void setData(long data){
        this.data = data;
    }
//    public void setData(GregorianCalendar data) {
//        this.data = data;
//    }

//    public void setData(GregorianCalendar calendar){
//        data = calendar.getTimeInMillis();
//    }

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

    public void setInsegnamentoLink(String insegnamentoLink) {
        this.insegnamentoLink = insegnamentoLink;
    }

    //    public List<Lezione> getLezioniProva() {
//
//        List<Lezione> lezioni = new ArrayList<>();
//
//        for(int i = 0; i < 20; i++) {
//            GregorianCalendar calendar = new GregorianCalendar();
//            calendar.add(Calendar.DATE, i);
//            lezioni.add(new Lezione(0, calendar, "10:00", "12:00",
//                                        "argomento", "insegnamento" + (i+1)));
//        }
//
//
//        return lezioni;
//
//    }

    @Override
    public String toString() {
        return "Lezione{" +
                "numPresenze=" + numPresenze +
                ", data=" + data +
                ", oraInizio='" + oraInizio + '\'' +
                ", oraFine='" + oraFine + '\'' +
                ", argomento='" + argomento + '\'' +
                ", insegnamento='" + insegnamento + '\'' +
                '}';
    }
}


