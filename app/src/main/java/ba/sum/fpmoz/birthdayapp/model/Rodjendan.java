package ba.sum.fpmoz.birthdayapp.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Rodjendan {
    public String naziv;
    public String datum;
    public String poklon;
    public String slika;


    public Rodjendan() {
    }

    public Rodjendan(String naziv, String datum, String poklon, String slika) {
        this.naziv = naziv;
        this.datum = datum;
        this.poklon = poklon;
        this.slika = slika;

    }

    public String getNaziv() {
        return naziv;
    }


    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getPoklon() {
        return poklon;
    }

    public void setPoklon(String poklon) {
        this.poklon = poklon;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

}
