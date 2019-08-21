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
}
