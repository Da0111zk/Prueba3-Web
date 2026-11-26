package cl.miespacio.security;

import org.springframework.security.core.Authentication;

public final class Sesion {
    private Sesion() {
    }

    public static boolean esAdmin(Authentication autenticacion) {
        return autenticacion.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
