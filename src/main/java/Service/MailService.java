package Service;

import Module.Membre;
import Module.Livre;

import java.util.List;
import java.util.Properties;
import java.awt.HeadlessException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;


public class MailService {

    private String messageContent(String nom, List<Livre> livres, String dateLimite){
        String body1 = "<p>Bonjour "+nom+",</p>"
                + "<p>Nous espérons que vous avez apprécié les livres que vous avez empruntés à la bibliothèque municipale. Nous souhaitons vous rappeler que la date limite pour rendre ces livres approche.</p>"
                + "<p><strong>Détails des livres empruntés :</strong></p>"
                + "<ul>";

        String contentLivre = "";
        int index = 1;

        for (Livre livre : livres) {
            contentLivre += "<li><strong>Titre du livre "+index+"</strong> : "+livre.getDesignation()+" ("+livre.getExemplaire()+")</li>";
            index++;
        }

        String body2 = "</ul>"
                + "<p><strong>Date limite de retour</strong> : "+dateLimite+"</p>"
                + "<p>Afin d'éviter toute pénalité de retard, nous vous encourageons à retourner les livres avant cette date. Si vous avez besoin de plus de temps, veuillez nous contacter pour discuter de la possibilité de prolonger la période de prêt.</p>"
                + "<p>Vous pouvez retourner les livres durant nos heures d'ouverture :</p>"
                + "<ul>"
                + "<li>Lundi à vendredi : 7h00 - 18h00</li>"
                + "<li>Samedi : 7h00 - 12h00</li>"
                + "<li>Dimanche : Fermé</li>"
                + "</ul>"
                + "<p>Nous vous remercions de votre coopération et nous espérons vous revoir bientôt à la bibliothèque.</p>"
                + "<p>Cordialement,</p>"
                + "<p>Jean RAKOTO<br>Bibliothèque Municipale de Fianarantsoa<br></p>";;

        return body1 + contentLivre + body2;
    }


    public boolean sendMail(Membre membre, List<Livre> livres, String dateLimite) {

        // Configurations pour la connexion au serveur de messagerie
        String FromEmail = "angelonambs@gmail.com";
        String FromEmailPasswords = "dtvkkemmndpznmzw";
        String subject = "Rappel de la date limite pour rendre les livres empruntés";

        // Paramètres SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
//        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.port", "587");
//        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        // Créer une session avec authentification
        try{
            Session session = Session.getDefaultInstance(properties, new jakarta.mail.Authenticator() {
                protected PasswordAuthentication  getPasswordAuthentication(){
                    return new PasswordAuthentication(FromEmail, FromEmailPasswords) ;
                }
            });

            // Créer un objet MimeMessage
            MimeMessage message = new MimeMessage(session);

            // Définir les paramètres de l'email
            message.setFrom(new InternetAddress(FromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(membre.getEmail()));
            message.setSubject(subject);

            // Définir le corps de l'email
            message.setContent(this.messageContent(membre.getPrenoms(), livres, dateLimite), "text/html");

            // Envoyer l'email
            Transport.send(message);

            System.out.println("Email envoyé avec succès");
            return true;


        }catch(HeadlessException | MessagingException ex){
            System.out.println(ex);
            throw new RuntimeException(ex);
        }

    }
}
