package it.uniba.maw.dibapp.model;

import java.util.Date;

public class Utente {

    private int id, matricola, annoImmatricolazione;
    private String nome, cognome, email, tipo, cdl;
    private Date dataNascita;

    public Utente(int id, int matricola, int annoImmatricolazione, String nome, String cognome, String email, String tipo, String cdl, Date dataNascita) {
        this.id = id;
        this.matricola = matricola;
        this.annoImmatricolazione = annoImmatricolazione;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.tipo = tipo;
        this.cdl = cdl;
        this.dataNascita = dataNascita;
    }

    public Utente() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatricola() {
        return matricola;
    }

    public void setMatricola(int matricola) {
        this.matricola = matricola;
    }

    public int getAnnoImmatricolazione() {
        return annoImmatricolazione;
    }

    public void setAnnoImmatricolazione(int annoImmatricolazione) {
        this.annoImmatricolazione = annoImmatricolazione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCdl() {
        return cdl;
    }

    public void setCdl(String cdl) {
        this.cdl = cdl;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }
}
