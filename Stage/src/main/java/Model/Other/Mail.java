package Model.Other;

import Controller.detailsController.AffairDetailsController;
import Model.Enum.NotifType;
import Model.Pojo.Demandeur;
import Model.Pojo.User;
import View.Dialog.Other.Notification;
import javafx.application.Platform;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Properties;

public class Mail {
    private final static String MAILER_VERSION = "Java";
    Session newSession = null;
    MimeMessage mimeMessage = null;
    private String[] emailReceipients;
    private String emailSubject;
    private String emailBody;

    public Mail(String[] emailReceipients, String emailSubject, String emailBody) throws MessagingException, IOException {
        setupServerProperties();
        draftEmail(emailReceipients, emailSubject, emailBody);
        sendEmail();
    }

    public void sendEmail() throws MessagingException {
        String fromUser = "cirdomaToamasinaI@gmail.com";  //Enter sender email id
        String fromUserPassword = "Aj!30071999";  //Enter sender gmail password , this will be authenticated by gmail smtp server
        String emailHost = "smtp.gmail.com";
        Transport transport = newSession.getTransport("smtp");
        transport.connect(emailHost,fromUser,fromUserPassword);
        transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        transport.close();
    }

    public MimeMessage draftEmail(String[] emailReceipients, String emailSubject, String emailBody) throws AddressException, MessagingException, IOException {
        this.emailReceipients = emailReceipients;
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        mimeMessage = new MimeMessage(newSession);
        for (int i =0 ;i<emailReceipients.length;i++)
        { mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailReceipients[i])); }
        mimeMessage.setSubject(emailSubject);
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(emailBody,"html/text");
        MimeMultipart multiPart = new MimeMultipart();
        multiPart.addBodyPart(bodyPart);
        mimeMessage.setContent(multiPart);
        return mimeMessage;
    }

    public void setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        newSession = Session.getDefaultInstance(properties,null);
    }

    public void setEmailReceipients(String[] emailReceipientsNouveau){
        this.emailReceipients = emailReceipientsNouveau;
    }

    public void setEmailSubject(String emailSubjectNouveau){
        this.emailSubject = emailSubjectNouveau;
    }

    public void setEmailBody(String emailBodyNouveau){
        this.emailBody = emailBodyNouveau;
    }

    public static void send(){
        Demandeur demandeur = AffairDetailsController.getAffaire().getDemandeur();
        String nom = demandeur.getNom();
        String prenom = demandeur.getPrenom();
        User redacteur = AffairDetailsController.getAffaire().getRedacteur();
        String nomR = redacteur.getNom();
        String prenomR = redacteur.getPrenom();
        String[] emailReceipients = {demandeur.getEmail()};  //Enter list of email recepients
        String emailBody = "Salama tompoko ,Mampahafantarana anao izahay ato amin'ny sampan-draharahan'ny fananan-tany fa i "+nomR+" "+prenomR+" no"+
                " mpiasa tompon-andraikitra amin'ny atontan-taratasinao manomboka izao . misaotra tompoko !";
        String emailSubject = "";
        try {
            new Mail(emailReceipients,emailSubject,emailBody);
            String m = " Email envoyé avec succès à "+nom+" "+prenom;
            Notification.getInstance(m, NotifType.SUCCESS).showNotif();
        } catch (MessagingException |IOException e) { e.printStackTrace(); }
    }

}
