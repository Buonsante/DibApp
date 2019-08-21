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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnnoDiCorso() {
        return annoDiCorso;
    }

    public void setAnnoDiCorso(int annoDiCorso) {
        this.annoDiCorso = annoDiCorso;
    }

    public int getAnnoAccademico() {
        return annoAccademico;
    }

    public void setAnnoAccademico(int annoAccademico) {
        this.annoAccademico = annoAccademico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCdl() {
        return cdl;
    }

    public void setCdl(String cdl) {
        this.cdl = cdl;
    }

    public String getCorso() {
        return corso;
    }

    public void setCorso(String corso) {
        this.corso = corso;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }
}
