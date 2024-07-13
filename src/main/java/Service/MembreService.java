package Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Module.Membre;
import config.Database;
import org.jetbrains.annotations.NotNull;

public class MembreService {
    private final Connection connection;

    public MembreService() throws SQLException, ClassNotFoundException {
        this.connection = Database.connection();
    }

    public List<Membre> getAll(){
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM membre");

            ArrayList<Membre> membres = new ArrayList<Membre>();

            while (rs.next()) {
                int idMembre = rs.getInt("idMembre");
                String nom = rs.getString("nom");
                String prenoms = rs.getString("prenoms");
                String email = rs.getString("email");
                String sexe = rs.getString("sexe");
                String adresse = rs.getString("adresse");
                java.sql.Date dateNaissance = rs.getDate("dateNaissance");
                Membre membre = new Membre(idMembre, nom, prenoms, sexe, email, adresse, dateNaissance);
                membres.add(membre);
            }
            return membres;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public List<Membre> getNotReturnBook(){
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT membre.* FROM preter, membre WHERE preter.rendu = false AND preter.dateRetour < current_date AND membre.idMembre = preter.idPers GROUP BY preter.idPers");

            ArrayList<Membre> membres = new ArrayList<Membre>();

            while (rs.next()) {
                int idMembre = rs.getInt("idMembre");
                String nom = rs.getString("nom");
                String prenoms = rs.getString("prenoms");
                String email = rs.getString("email");
                String sexe = rs.getString("sexe");
                String adresse = rs.getString("adresse");
                java.sql.Date dateNaissance = rs.getDate("dateNaissance");
                Membre membre = new Membre(idMembre, nom, prenoms, sexe, email, adresse, dateNaissance);
                membres.add(membre);
            }
            return membres;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    public Membre getById(int id){
        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM membre WHERE idMembre = "+ id);

            List<Membre> membres = getMembre(rs);
            return membres.get(0);
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            throw new RuntimeException(e);
        }
    }

    private List<Membre> getMembre(ResultSet rs) throws SQLException {
        ArrayList<Membre> membres = new ArrayList<Membre>();

        while (rs.next()) {
            int idMembre = rs.getInt("idMembre");
            String nom = rs.getString("nom");
            String prenoms = rs.getString("prenoms");
            String email = rs.getString("email");
            String sexe = rs.getString("sexe");
            String adresse = rs.getString("adresse");
            Date dateNaissance = rs.getDate("dateNaissance");
            Membre membre = new Membre(idMembre, nom, prenoms, sexe, email, adresse, dateNaissance);
            membres.add(membre);
        }
        return membres;
    }

    public boolean add(@NotNull Membre membre){
        try {
            String sql = "INSERT INTO membre (nom, prenoms, sexe, email, adresse, dateNaissance) " +
                    "VALUES (?, ?, ?, ? ,?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, membre.getNom());
            preparedStatement.setString(2, membre.getPrenoms());
            preparedStatement.setString(3, membre.getSexe());
            preparedStatement.setString(4, membre.getEmail());
            preparedStatement.setString(5, membre.getAdresse());
            preparedStatement.setDate(6, membre.getDateNaissance());
            preparedStatement.executeUpdate();
            System.out.println("Add Success !!");
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(@NotNull Membre membre){
        try {
            String sql = "UPDATE membre " +
                    "SET nom = ?," +
                        "prenoms = ?," +
                        "sexe = ?," +
                        "email = ?," +
                        "adresse= ?," +
                        "dateNaissance = ?"+
                    "WHERE idMembre = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
            preparedStatement.setString(1, membre.getNom());
            preparedStatement.setString(2, membre.getPrenoms());
            preparedStatement.setString(3, membre.getSexe());
            preparedStatement.setString(4, membre.getEmail());
            preparedStatement.setString(5, membre.getAdresse());
            preparedStatement.setDate(6, membre.getDateNaissance());
            preparedStatement.setInt(7, membre.getIdMembre());
            preparedStatement.executeUpdate();
            System.out.println("Update Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void delete(int idMembre){
        try {
            String sql = "DELETE FROM membre WHERE idMembre = " + idMembre;
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Delete Success !!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
