package cl.miespacio.config;

import cl.miespacio.model.Resena;
import cl.miespacio.model.Rol;
import cl.miespacio.model.Servicio;
import cl.miespacio.model.Sucursal;
import cl.miespacio.model.Usuario;
import cl.miespacio.repository.ResenaRepository;
import cl.miespacio.repository.ServicioRepository;
import cl.miespacio.repository.SucursalRepository;
import cl.miespacio.repository.UsuarioRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatosIniciales implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(DatosIniciales.class);

    private final ServicioRepository servicios;
    private final SucursalRepository sucursales;
    private final ResenaRepository resenas;
    private final UsuarioRepository usuarios;
    private final PasswordEncoder codificador;
    private final String adminEmail;
    private final String adminPassword;

    public DatosIniciales(ServicioRepository servicios, SucursalRepository sucursales, ResenaRepository resenas,
                          UsuarioRepository usuarios, PasswordEncoder codificador,
                          @Value("${app.admin.email}") String adminEmail,
                          @Value("${app.admin.password:}") String adminPassword) {
        this.servicios = servicios;
        this.sucursales = sucursales;
        this.resenas = resenas;
        this.usuarios = usuarios;
        this.codificador = codificador;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        crearAdministrador();
        if (servicios.count() == 0) {
            servicios.save(servicio("Locker", "Ofrecemos espacios seguros y confiables tipo lockers para almacenar tus pertenencias.",
                    "Almacenamiento de pertenencias", "50cm x 50cm", "/img/locker.jpg"));
            servicios.save(servicio("Mini bodega", "Bodegas de tamaño pequeño para almacenar objetos de dimensiones reducidas.",
                    "Mini bodega", "1.5mt x 1.5mt", "/img/bodega-mini.jpg"));
            servicios.save(servicio("Bodega grande", "Bodegas de tamaño amplio para almacenar objetos de dimensiones mayores.",
                    "Bodega grande", "3mt x 3mt", "/img/bodega-grande.jpg"));
            servicios.save(servicio("Bodega a medida", "Espacio adaptado a tus necesidades. Nuestro equipo te asesora según el volumen de tus pertenencias.",
                    "Asesoría personalizada", "Según cotización", "/img/bodega-medida.jpg"));
        }
        if (sucursales.count() == 0) {
            sucursales.save(sucursal("Sucursal Norte - Quilicura", "Av. Américo Vespucio 1234, Quilicura",
                    "Lunes a Viernes 8:00 - 19:00 hrs", "24/7 con tarjeta digital", "+56 9 1234 5678",
                    "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3332.221764372225!2d-70.7325608!3d-33.3653181!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9662c1404113cb63%3A0xb3ff7ed01c706d9a!2sQuilicura%2C%20Regi%C3%B3n%20Metropolitana!5e0!3m2!1ses!2scl!4v1710000000000!5m2!1ses!2scl"));
            sucursales.save(sucursal("Sucursal Centro - Santiago", "Av. Libertador Bernardo O'Higgins 2500, Santiago",
                    "Lunes a Sábado 9:00 - 18:00 hrs", "24/7 con tarjeta digital", "+56 9 8765 4321",
                    "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3329.3108985923985!2d-70.6653199!3d-33.4447387!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9662c5a22f3050bd%3A0x1d37c8a6f874c728!2sSantiago%2C%20Regi%C3%B3n%20Metropolitana!5e0!3m2!1ses!2scl!4v1710000000000!5m2!1ses!2scl"));
        }
        if (resenas.count() == 0) {
            resenas.save(resena("Agustin Moya Rios", "Me lo recomendo un amigo, desde entonces yo se lo he recomendado a mis amigos familiares", 5));
            resenas.save(resena("David Argos Guzman", "Llevo durante meses con esta empresa y nunca he tenido un solo problema.", 5));
            resenas.save(resena("Gonzalo Bustamante Isla", "La gestion y la persona que me atendio fue muy rapido y expedito, lo recomendaria sin dudarlo.", 5));
        }
    }

    private void crearAdministrador() {
        if (usuarios.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }
        boolean generada = adminPassword == null || adminPassword.isBlank();
        String clave = generada ? generarClave() : adminPassword;
        Usuario admin = new Usuario();
        admin.setNombre("Administrador");
        admin.setEmail(adminEmail.toLowerCase());
        admin.setPasswordHash(codificador.encode(clave));
        admin.setRol(Rol.ADMIN);
        usuarios.save(admin);
        if (generada) {
            LOG.warn("Administrador inicial creado -> correo: {} | contraseña generada: {} (se muestra solo esta vez; define ADMIN_PASSWORD para fijarla)",
                    adminEmail, clave);
        } else {
            LOG.info("Administrador inicial creado con el correo {}", adminEmail);
        }
    }

    private static String generarClave() {
        String alfabeto = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        SecureRandom azar = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            sb.append(alfabeto.charAt(azar.nextInt(alfabeto.length())));
        }
        return sb.toString();
    }

    private static Servicio servicio(String nombre, String descripcion, String categoria, String tamano, String imagen) {
        Servicio s = new Servicio();
        s.setNombre(nombre);
        s.setDescripcion(descripcion);
        s.setCategoria(categoria);
        s.setTamano(tamano);
        s.setImagen(imagen);
        return s;
    }

    private static Sucursal sucursal(String nombre, String direccion, String horario, String acceso, String telefono, String mapaUrl) {
        Sucursal s = new Sucursal();
        s.setNombre(nombre);
        s.setDireccion(direccion);
        s.setHorario(horario);
        s.setAcceso(acceso);
        s.setTelefono(telefono);
        s.setMapaUrl(mapaUrl);
        return s;
    }

    private static Resena resena(String autor, String texto, int evaluacion) {
        Resena r = new Resena();
        r.setAutor(autor);
        r.setTexto(texto);
        r.setEvaluacion(evaluacion);
        r.setFecha(LocalDateTime.now());
        return r;
    }
}
