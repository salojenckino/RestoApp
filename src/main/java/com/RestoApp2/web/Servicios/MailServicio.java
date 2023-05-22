package com.RestoApp2.web.Servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServicio {

    @Autowired
    private JavaMailSender javaMailSender;

    /*Al marcarlo como async, el hilo de ejeucion no espera a que se termine de enviar el mail, lo ejecuta
      en un hilo paralelo. Esto que genera que el usuario no tenga que esperar a que se termine de enviar
      el mail para tener respuesta sino que la RESPUESTA ES INMEDITA
      Lo que se hace async es abrir un nuevo hilo de ejecucion para enviar el mail y al usuario le da la rta inmediata
      y paralelamente se va ir enviando el correo y el usu no tiene q esperar.*/
    @Async
    public void enviarMail(String cuerpo, String titulo, String mail) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(mail);
        mensaje.setFrom("restoapp4@gmail.com");
        mensaje.setSubject(titulo);
        mensaje.setText(cuerpo);

        javaMailSender.send(mensaje);
    }
}
