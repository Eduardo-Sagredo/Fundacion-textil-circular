package cl.textilcircular_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(
            JavaMailSender mailSender
    ) {

        this.mailSender =
                mailSender;
    }


    public void enviarCorreoRecuperacion(
            String destinatario,
            String token
    ) {

        try {

            MimeMessage mensaje =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            mensaje,
                            true,
                            "UTF-8"
                    );

            helper.setTo(
                    destinatario
            );

            helper.setSubject(
                    "Recuperación de contraseña - Textil Circular"
            );


            String html = """
                    <!DOCTYPE html>
                    <html lang="es">

                    <body style="
                        margin:0;
                        padding:0;
                        background-color:#f5f1e8;
                        font-family:Arial, Helvetica, sans-serif;
                    ">

                        <div style="
                            max-width:600px;
                            margin:30px auto;
                            background-color:#ffffff;
                            border-radius:16px;
                            overflow:hidden;
                            box-shadow:0 4px 16px rgba(0,0,0,0.12);
                        ">

                            <div style="
                                background-color:#f5f1e8;
                                padding:25px;
                                text-align:center;
                            ">

                                <img
                                    src="cid:logoTextilCircular"
                                    alt="Textil Circular"
                                    width="180"
                                    style="
                                        display:block;
                                        margin:0 auto;
                                        width:180px;
                                        height:auto;
                                    "
                                />

                            </div>


                            <div style="
                                padding:35px;
                                color:#222222;
                            ">

                                <h2 style="
                                    margin-top:0;
                                    color:#00a9a9;
                                    text-align:center;
                                ">
                                    Recuperación de contraseña
                                </h2>


                                <p style="
                                    font-size:16px;
                                    line-height:1.6;
                                ">
                                    Hola,
                                </p>


                                <p style="
                                    font-size:16px;
                                    line-height:1.6;
                                ">
                                    Recibimos una solicitud para restablecer
                                    la contraseña de tu cuenta en
                                    <strong>Textil Circular</strong>.
                                </p>


                                <p style="
                                    font-size:16px;
                                    line-height:1.6;
                                ">
                                    Ingresa el siguiente código
                                    en la aplicación:
                                </p>


                                <div style="
                                    background-color:#f7f7f7;
                                    border:2px solid #00a9a9;
                                    border-radius:12px;
                                    padding:22px;
                                    margin:25px 0;
                                    text-align:center;
                                ">

                                    <span style="
                                        font-size:34px;
                                        font-weight:bold;
                                        letter-spacing:8px;
                                        color:#1f2924;
                                    ">
                                        %s
                                    </span>

                                </div>


                                <p style="
                                    font-size:15px;
                                    line-height:1.6;
                                ">
                                    Este código tiene una duración de
                                    <strong>15 minutos</strong>.
                                </p>


                                <p style="
                                    font-size:15px;
                                    line-height:1.6;
                                ">
                                    Por seguridad, no compartas este código
                                    con otras personas.
                                </p>


                                <p style="
                                    font-size:15px;
                                    line-height:1.6;
                                ">
                                    Si no solicitaste este cambio,
                                    puedes ignorar este correo.
                                </p>

                            </div>


                            <div style="
                                background-color:#1f2924;
                                padding:20px;
                                text-align:center;
                                color:#ffffff;
                                font-size:13px;
                            ">

                                Fundación Textil Circular

                                <br><br>

                                Reutilizar • Transformar • Circular

                            </div>

                        </div>

                    </body>

                    </html>
                    """.formatted(token);


            helper.setText(
                    html,
                    true
            );


            ClassPathResource logo =
                    new ClassPathResource(
                            "static/images/logo_textil_circular.png"
                    );


            helper.addInline(
                    "logoTextilCircular",
                    logo,
                    "image/png"
            );


            mailSender.send(
                    mensaje
            );


        } catch (
                MessagingException e
        ) {

            throw new RuntimeException(
                    "Error al crear el correo de recuperación",
                    e
            );
        }
    }
}