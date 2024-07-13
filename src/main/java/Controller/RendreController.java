package Controller;

import Service.RendreService;
import com.google.gson.Gson;
import config.Header;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.annotation.*;
import Module.Rendre;
import Util.Conversion;

@WebServlet(name = "rendre", value = "/rendre")
public class RendreController extends HttpServlet {
    private RendreService rendreService;
    private final Gson gson = new Gson();
    private final Map<Object, Object> responseAPI = new HashMap<>();

    @Override
    public void init() {
        try {
            rendreService = new RendreService();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String idMembre = request.getParameter("idMembre");
            List<Rendre> rendreList;
            if(idMembre != null){
                rendreList = rendreService.getHistoricByPers(idMembre);
            }else {
                rendreList = rendreService.getAll();
            }



            // Set HTTP status and message in response
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");

            PrintWriter out = response.getWriter();
            out.println(gson.toJson(rendreList));
            out.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String idMembreStr = request.getParameter("idMembre");
        String idPretStr = request.getParameter("idPret");
        String dateRenduStr = request.getParameter("dateRendu");
        responseAPI.clear();

        if(idMembreStr != null && idPretStr != null && dateRenduStr !=  null) {
            idMembreStr= idMembreStr.trim();
            idPretStr = idPretStr.trim();
            dateRenduStr = dateRenduStr.trim();


            if(!idMembreStr.isEmpty() && !idPretStr.isEmpty() && !dateRenduStr.isEmpty()) {
                int idMembre = Conversion.convertStringToNumber(idMembreStr);
                int idPret = Conversion.convertStringToNumber(idPretStr);
                java.sql.Timestamp dateRendu = Conversion.convertDateStringToSqlTimestamp(dateRenduStr);
                Rendre rendre = new Rendre(idMembre, idPret, dateRendu);
                rendreService.add(rendre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Add rendre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "idMembre, idPret, dateRendu: ces valeurs de doivent pas être vides");
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
        String idRenduStr = request.getParameter("idRendu");
        String idMembreStr = request.getParameter("idMembre");
        String idPretStr = request.getParameter("idPret");
        String dateRenduStr = request.getParameter("dateRendu");
        responseAPI.clear();

        if(idRenduStr != null && idMembreStr != null && idPretStr != null && dateRenduStr !=  null) {
            idRenduStr = idRenduStr.trim();
            idMembreStr = idMembreStr.trim();
            idPretStr = idPretStr.trim();
            dateRenduStr = dateRenduStr.trim();

            if(!idRenduStr.isEmpty() && !idMembreStr.isEmpty() && !idPretStr.isEmpty() && !dateRenduStr.isEmpty()) {
                int idRendu = Conversion.convertStringToNumber(idRenduStr);
                int idMembre = Conversion.convertStringToNumber(idMembreStr);
                int idPret = Conversion.convertStringToNumber(idPretStr);
                java.sql.Timestamp dateRendu = Conversion.convertDateStringToSqlTimestamp(dateRenduStr);

                Rendre rendre = new Rendre(idRendu, idMembre, idPret, dateRendu);
                rendreService.update(rendre);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Update rendre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "idRendu, idMembre, idPret, dateRendu: ces valeurs de doivent pas être vides");
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
        String idRenduStr = request.getParameter("idRendu");
        responseAPI.clear();

        if(idRenduStr != null) {
            idRenduStr = idRenduStr.trim();
            if(!idRenduStr.isEmpty()) {
                int idRendu = Conversion.convertStringToNumber(idRenduStr);
                rendreService.delete(idRendu);

                this.responseAPI.put("success", true);
                this.responseAPI.put("message", "Delete rendre successfully !!");
            }else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                this.responseAPI.put("success", false);
                this.responseAPI.put("message", "Verifier le idRendu");
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