package Module;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.text.SimpleDateFormat;

public class Membre {
    private int idMembre;
    private String nom;
    private String prenoms;
    private String sexe;
    private String email;
    private String adresse;
    private Date dateNaissance;
    private String dateNaissanceFormated;
    private int age;

    public Membre(String nom, String prenoms, String sexe, String email, String adresse, Date dateNaissance) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.nom = nom;
        this.prenoms = prenoms;
        this.sexe = sexe;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.dateNaissanceFormated = sdf.format(dateNaissance);
        this.setAge();
    }

    public Membre(int idMembre, String nom, String prenoms, String sexe, String email, String adresse, Date dateNaissance) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.idMembre = idMembre;
        this.nom = nom;
        this.prenoms = prenoms;
        this.sexe = sexe;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.dateNaissanceFormated = sdf.format(dateNaissance);
        this.setAge();
    }

    public int getIdMembre() {
        return idMembre;
    }

    public void setIdMembre(int idMembre) {
        this.idMembre = idMembre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public void setPrenoms(String prenoms) {
        this.prenoms = prenoms;
    }

    public String getDateNaissanceFormated() {
        return dateNaissanceFormated;
    }

    public void setDateNaissanceFormated(String dateNaissanceFormated) {
        this.dateNaissanceFormated = dateNaissanceFormated;
    }

    public int getAge() {
        return age;
    }

    public void setAge() {
        LocalDate birthLocalDAte = dateNaissance.toLocalDate();
        LocalDate today = LocalDate.now();
        this.age = Period.between(birthLocalDAte, today).getYears();
    }
}
