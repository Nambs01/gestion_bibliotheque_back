package Module;

import java.io.Serializable;

public class Livre implements Serializable {
    private int id;
    private String designation;
    private int exemplaire;

    public Livre(int id, String designation, int exemplaire) {
        this.id = id;
        this.designation = designation.trim();
        this.exemplaire = exemplaire;
    }

    public Livre(String designation, int exemplaire) {
        this.designation = designation;
        this.exemplaire = exemplaire;
    }

    public int getId() {
        return id;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public int getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(int exemplaire) {
        this.exemplaire = exemplaire;
    }
}
