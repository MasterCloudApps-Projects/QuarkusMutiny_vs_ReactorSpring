package es.urjc.rest.client;

public class CatFact {

    private String fact;
    private int length;

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public CatFact(String fact, int length) {
        this.fact = fact;
        this.length = length;
    }

    public CatFact() {
    }
}
