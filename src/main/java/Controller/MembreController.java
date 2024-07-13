package Controller;

import Service.MembreService;
import com.google.gson.Gson;
import config.Header;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.annotation.*;
import Module.Membre;
import Util.Conversion;

@WebServlet(name = "membre", value = "/membre")
public class MembreController extends HttpServlet {
    private MembreService membreService;
    private final Gson gson = new Gson();
    private final Map<Object, Object> responseAPI = new HashMap<>();

    @Override
    public void init() {
        try {
            membreService = new MembreService();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nonRendre = request.getParameter("nonRendre");
        List<Membre> membres;

        if (nonRendre != null && !nonRendre.isEmpty()) membres = membreService.getNotReturnBook();
        else membres = membreService.getAll();;
        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(membres));
        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nom = request.getParameter("nom");
        String prenoms = request.getParameter("prenoms");
        String sexe = request.getParameter("sexe");
        String adresse = request.getParameter("adresse");
        String email = request.getParameter("email");
        String dateNaissanceStr = request.getParameter("dateNaissance");
        responseAPI.clear();
        System.out.println(nom);
        System.out.println(prenoms);
        System.out.println(sexe);
        System.out.println(adresse);
        System.out.println(email);
        System.out.println(dateNaissanceStr);

        if(nom != null && sexe != null && adresse !=  null && email != null && dateNaissanceStr != null ) {
            nom = nom.trim();
            sexe = sexe.trim();
            adresse = adresse.trim();
            email = email.trim();
            java.sql.Date dateNaissance = Conversion.convertDateStringToSqlDate(dateNaissanceStr);

            if(!nom.isEmpty() && !sexe.isEmpty() && !adresse.isEmpty() && !email.isEmpty()) {
                Membre membre = new Membre(nom, prenoms, sexe, email, adresse, dateNaissance);
                membreService.add(membre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Add membre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Nom, sexe, adresse, dateNaissance: ces valeurs de doivent pas être vides");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();

    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idMembreStr = request.getParameter("idMembre");
        String nom = request.getParameter("nom");
        String prenoms = request.getParameter("prenoms");
        String sexe = request.getParameter("sexe");
        String adresse = request.getParameter("adresse");
        String email = request.getParameter("email");
        String dateNaissanceStr = request.getParameter("dateNaissance");
        responseAPI.clear();

        if(idMembreStr != null && nom != null && sexe != null && adresse !=  null && email != null && dateNaissanceStr != null ) {
            nom = nom.trim();
            sexe = sexe.trim();
            adresse = adresse.trim();
            email = email.trim();
            java.sql.Date dateNaissance = Conversion.convertDateStringToSqlDate(dateNaissanceStr);

            if(!nom.isEmpty() && !sexe.isEmpty() && !adresse.isEmpty() && !email.isEmpty()) {
                int idMembre = Integer.parseInt(idMembreStr);
                Membre membre = new Membre(idMembre, nom, prenoms, sexe, email, adresse, dateNaissance);
                membreService.update(membre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Update membre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Nom, sexe, adresse, dateNaissance: ces valeurs de doivent pas être vides");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idMembreStr = request.getParameter("idMembre");
        responseAPI.clear();

        if(idMembreStr != null) {
            idMembreStr = idMembreStr.trim();
            if(!idMembreStr.isEmpty()) {
                int idMembre = Conversion.convertStringToNumber(idMembreStr);
                membreService.delete(idMembre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Delete rendre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Verifier le idMembre");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            this.responseAPI.put("success", false);
            this.responseAPI.put("message", "Manque de parametre");
        }

        PrintWriter out = Header.setResponseJson(response);
        out.println(gson.toJson(this.responseAPI));
        out.flush();
    }
}