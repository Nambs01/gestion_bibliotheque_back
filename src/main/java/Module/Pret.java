package Module;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Pret implements Serializable {
    private int id;
    private int idPers;
    private int idLivre1;
    private int nbrLivre1;
    private String idLivre2;
    private String nbrLivre2;
    private Timestamp datePret;
    private Date dateRetour;
    private boolean rendu;

    public Pret(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPers() {
        return idPers;
    }

    public void setIdPers(int idPers) {
        this.idPers = idPers;
    }


    public int getIdLivre1() {
        return idLivre1;
    }

    public void setIdLivre1(int idLivre1) {
        this.idLivre1 = idLivre1;
    }

    public int getNbrLivre1() {
        return nbrLivre1;
    }

    public void setNbrLivre1(int nbrLivre1) {
        this.nbrLivre1 = nbrLivre1;
    }

    public String getIdLivre2() {
        return idLivre2;
    }

    public void setIdLivre2(String idLivre2) {
        this.idLivre2 = idLivre2;
    }

    public String getNbrLivre2() {
        return nbrLivre2;
    }

    public void setNbrLivre2(String nbrLivre2) {
        this.nbrLivre2 = nbrLivre2;
    }

    public Timestamp getDatePret() {
        return datePret;
    }

    public void setDatePret(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            java.util.Date javaDate = formatter.parse(dateString);
            this.datePret = new java.sql.Timestamp(javaDate.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public boolean isRendu() {
        return rendu;
    }

    public void setRendu(boolean rendu) {
        this.rendu = rendu;
    }
}
