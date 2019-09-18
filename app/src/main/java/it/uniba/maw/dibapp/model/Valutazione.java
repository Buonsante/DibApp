package it.uniba.maw.dibapp.model;

public class Valutazione {

    private float rating;
    private String commento;
    private boolean publicComment;
    private String data;


    public Valutazione(float rating, String commento, boolean publicComment, String data) {
        this.rating = rating;
        this.commento = commento;
        this.publicComment = publicComment;
        this.data = data;
    }

    public Valutazione() {

    }


    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public boolean isPublicComment() {
        return publicComment;
    }

    public void setPublicComment(boolean publicComment) {
        this.publicComment = publicComment;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}




