package es.urjc.rest.client;

public class CatFact {

    private String fact;
    private int lenght;

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public CatFact(String fact, int lenght) {
        this.fact = fact;
        this.lenght = lenght;
    }

    public CatFact() {
    }
}
