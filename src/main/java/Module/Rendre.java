package Module;

import java.sql.Timestamp;

public class Rendre {
    private int idRendu;
    private int idMembre;
    private int idPret;
    private java.sql.Timestamp dateRendu;

    public Rendre(int idMembre, int idPret, Timestamp dateRendu) {
        this.idMembre = idMembre;
        this.idPret = idPret;
        this.dateRendu = dateRendu;
    }

    public Rendre(int idRendu, int idMembre, int idPret, Timestamp dateRendu) {
        this.idRendu = idRendu;
        this.idMembre = idMembre;
        this.idPret = idPret;
        this.dateRendu = dateRendu;
    }

    public int getIdRendu() {
        return idRendu;
    }

    public void setIdRendu(int idRendu) {
        this.idRendu = idRendu;
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public int getIdPret() {
        return idPret;
    }

    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    public Timestamp getDateRendu() {
        return dateRendu;
    }

    public void setDateRendu(Timestamp dateRendu) {
        this.dateRendu = dateRendu;
    }
}
