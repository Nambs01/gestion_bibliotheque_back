package config;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import Module.*;
import Util.Conversion;

public class Util {
    public static java.sql.Date add14Days(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
            Date date = formatter.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 14);
            date = cal.getTime();
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Pret> getPrets(ResultSet rs) throws SQLException {
        List<Pret> listPret = new ArrayList<Pret>();

        while (rs.next()) {
            Pret pret = new Pret();
            pret.setId(rs.getInt("id"));
            pret.setIdPers(rs.getInt("idPers"));
            pret.setIdLivre1(rs.getInt("idLivre1"));
            pret.setIdLivre2(rs.getString("idLivre2"));
            pret.setNbrLivre1(rs.getInt("nbrLivre1"));
            pret.setNbrLivre2(rs.getString("nbrLivre2"));
            pret.setDatePret(rs.getString("datePret"));
            pret.setDateRetour(rs.getDate("dateRetour"));
            pret.setRendu(rs.getBoolean("rendu"));
            listPret.add(pret);
        }
        return listPret;
    }

    public static List<Livre> getLivres(ResultSet rs) throws SQLException {
        ArrayList<Livre> livres = new ArrayList<Livre>();

        while (rs.next()) {
            int id = rs.getInt("id");
            String designation = rs.getString("designation");
            int exemplaire = rs.getInt("exemplaire");
            Livre livre = new Livre(id, designation, exemplaire);
            livres.add(livre);
        }
        return livres;
    }

    public static void generatePDF(Pret pret, Membre membre, List<Livre> livres) {
        String path = "C:\\Users\\Fanny Judicael\\Desktop\\pdf\\Réçu de prêt N°"+new Date().getTime()+".pdf";
        Document document = new Document();
        try {

            try {
                PdfWriter.getInstance(document, new FileOutputStream(path));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            document.open();

            Font font = new Font(Font.FontFamily.HELVETICA);

            Phrase titre = new Phrase("BIBLIOTHEQUE MUNICIPALE");
            titre.setFont(font);

            PdfPTable p1 = new PdfPTable(1);
            p1.setWidthPercentage(40);

            PdfPCell c1 = new PdfPCell(titre);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            p1.addCell(c1).setBorderWidth(0);
            document.add(p1);

            String sexe = null;
            if(membre.getSexe().equals("H")) sexe = "Homme";
            else if(membre.getSexe().equals("F")) sexe = "Femme";


            List<String> infoMembre = new ArrayList<>();
            infoMembre.add("Nom : " + membre.getNom().toUpperCase());
            infoMembre.add("Prénoms : " + membre.getPrenoms());
            infoMembre.add("Age : " + membre.getAge() + " ans");
            infoMembre.add("Sexe : " + sexe);
            infoMembre.add("Adresse : " + membre.getAdresse());
            infoMembre.add("Email : "+ membre.getEmail());

            for (String phrase : infoMembre) {
                Paragraph p = new Paragraph(phrase);
                p.setFont(font);
                document.add(p);
            }

            Paragraph p4 = new Paragraph(" ");
            document.add(p4);

            List<String> enTete = new ArrayList<>();
            enTete.add("Code livre");
            enTete.add("Intitulé");
            enTete.add("Nombre prêté");

            PdfPTable table = new PdfPTable(3);
            for(String s : enTete){
                Phrase p = new Phrase(s);
                p.setFont(font);

                PdfPCell cell = new PdfPCell(p);
                cell.setPadding(5);
                cell.setBackgroundColor(BaseColor.GRAY);

                table.addCell(cell);
            }

            for(Livre livre : livres){
                for(int i = 0; i<3; i++){
                    String str = switch (i) {
                        case 0 -> String.valueOf(livre.getId());
                        case 1 -> livre.getDesignation();
                        case 2 -> String.valueOf(livre.getExemplaire());
                        default -> null;
                    };
                    Phrase p = new Phrase(str);
                    p.setFont(font);

                    PdfPCell cell = new PdfPCell(p);
                    cell.setPadding(5);

                    table.addCell(cell);
                }
            }

            document.add(table);

            document.add(new Paragraph(" "));

            String datePret = Conversion.sqlDateToString(pret.getDatePret());
            String dateRetour = Conversion.sqlDateToString(pret.getDateRetour());

            List<String> dates = new ArrayList<>();
            dates.add("Prêté le : " + datePret);
            dates.add("Doit être rendu le : " + dateRetour);

            for (String phrase : dates) {
                Paragraph p = new Paragraph(phrase);
                p.setFont(font);
                document.add(p);
            }

            document.close();

        } catch (DocumentException ex) {
            throw new RuntimeException(ex);
        }
    }

}
