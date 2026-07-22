package com.example.SIGR.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Envoi d'email best-effort pour les notifications critiques/hautes.
 *
 * Tolérant à l'absence de configuration SMTP : tant que
 * spring.mail.host n'est pas renseigné (voir application.properties),
 * envoyer() se contente de logger et ne lève jamais d'exception — le
 * reste de l'application (notifications in-app) fonctionne pleinement
 * sans qu'aucun serveur mail ne soit configuré.
 */
@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.host:}")
    private String mailHost;

    @Value("${spring.mail.username:notifications@sigr.local}")
    private String expediteur;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean estConfigure() {
        return mailHost != null && !mailHost.isBlank();
    }

    public void envoyer(String destinataire, String sujet, String corps) {
        if (!estConfigure()) {
            logger.debug("Email non envoyé (SMTP non configuré) — destinataire={}, sujet={}", destinataire, sujet);
            return;
        }
        if (destinataire == null || destinataire.isBlank()) {
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(expediteur);
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(corps);
            mailSender.send(message);
        } catch (MailException e) {
            // Un incident SMTP ne doit jamais faire échouer la génération
            // des notifications in-app, seul le canal email est dégradé.
            logger.warn("Échec d'envoi d'email à {} : {}", destinataire, e.getMessage());
        }
    }
}
