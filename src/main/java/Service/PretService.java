package Service;

import config.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Module.Pret;
import config.Util;
import Module.Membre;
import Module.Livre;

public class PretService {
    private final Statement stmt;

    public PretService() throws SQLException, ClassNotFoundException {
        this.stmt = Database.connection().createStatement();
    }

    public List<Pret> getAll() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM preter");
            return Util.getPrets(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pret> getHistoricByPers(String idPers) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM preter WHERE idPers = '"+Integer.parseInt(idPers)+"' ORDER BY datePret DESC ");
            return Util.getPrets(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Pret getById(String id) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM preter WHERE id = "+Integer.parseInt(id));
            List<Pret> prets = Util.getPrets(rs);
            return prets.get(0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pret> getBetweenDate(String dateDeb, String dateFin) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM preter WHERE datePret BETWEEN '"+dateDeb+"' AND '"+dateFin+"' ORDER BY datePret DESC ");
            return Util.getPrets(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Pret pret) {
        try {
            String sql;
            if(!pret.getIdLivre2().isEmpty()){
                sql = "INSERT INTO preter (idPers, idLivre1, nbrLivre1, idLivre2, nbrLivre2, datePret, dateRetour) VALUES ("+
                        pret.getIdPers() +", "+
                        pret.getIdLivre1() +", "+
                        pret.getNbrLivre1() +", "+
                        pret.getIdLivre2() +", "+
                        pret.getNbrLivre2() +", '"+
                        pret.getDatePret() +"', '"+
                        pret.getDateRetour() +"')";
            }else {
                sql = "INSERT INTO preter (idPers, idLivre1, nbrLivre1, datePret, dateRetour) VALUES ("+
                        pret.getIdPers() +", "+
                        pret.getIdLivre1() +", "+
                        pret.getNbrLivre1() +", '"+
                        pret.getDatePret() +"', '"+
                        pret.getDateRetour() +"')";
            }

            this.stmt.executeUpdate(sql);

            new LivreService().updateExemplaire(pret.getIdLivre1(), pret.getNbrLivre1(), true);
            if(!pret.getIdLivre2().isEmpty())
                new LivreService().updateExemplaire(Integer.parseInt(pret.getIdLivre2()), Integer.parseInt(pret.getNbrLivre2()), true);

            System.out.println("Add Success !!");

            try {
                Membre membre = new MembreService().getById(pret.getIdPers());

                List<Livre> livres = new ArrayList<Livre>();

                String idLivre1 = String.valueOf(pret.getIdLivre1());
                Livre livre = new LivreService().get(idLivre1).get(0);
                livre.setExemplaire(pret.getNbrLivre1());

                livres.add(livre);
                if(!pret.getIdLivre2().isEmpty()){
                    livre = new LivreService().get(pret.getIdLivre2()).get(0);
                    livre.setExemplaire(Integer.parseInt(pret.getNbrLivre2()));
                    livres.add(livre);
                }
                Util.generatePDF(pret, membre, livres);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void isRendu(String id){
        try{
            this.stmt.executeUpdate("UPDATE preter SET rendu = true WHERE id = "+id);
            System.out.println("Rendu Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Pret pret) {
        try {
            String sql = "UPDATE preter SET idPers = "+ pret.getIdPers() +", " +
                    "idLivre1 = "+ pret.getIdLivre1() +", " +
                    "nbrLivre1 = "+ pret.getNbrLivre1() +", " +
                    "idLivre2 = "+ pret.getIdLivre2() +", " +
                    "nbrLivre2 = "+ pret.getNbrLivre2() +", " +
                    "datePret = '"+ pret.getDatePret() +"', " +
                    "dateRetour = '"+ pret.getDateRetour() + "' " +
                    "WHERE id = " + pret.getId();

            this.stmt.executeUpdate(sql);
            System.out.println("Update Success !!");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) {
        try {
            String sql = "DELETE FROM preter WHERE id = " + id;
            this.stmt.executeUpdate(sql);
            System.out.println("Delete Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
