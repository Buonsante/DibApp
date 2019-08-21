package it.uniba.maw.dibapp.model;

public class Insegnamento {

    private int id, annoDiCorso, annoAccademico;
    private String nome, cdl, corso, docente;

    public Insegnamento(int id, int annoDiCorso, int annoAccademico, String nome, String cdl, String corso, String docente) {
        this.id = id;
        this.annoDiCorso = annoDiCorso;
        this.annoAccademico = annoAccademico;
        this.nome = nome;
        this.cdl = cdl;
        this.corso = corso;
        this.docente = docente;
    }

    public Insegnamento() {

    }
}
