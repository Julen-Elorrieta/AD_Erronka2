package utils;

/**
 * Singleton para mantener la sesión del usuario actual
 */
public class SessionManager {
    
    private static SessionManager instance;
    private CheckLogin.UserData usuarioActual;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public void iniciarSesion(CheckLogin.UserData usuario) {
        this.usuarioActual = usuario;
    }
    
    public CheckLogin.UserData getUsuarioActual() {
        return usuarioActual;
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
    
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }
    
    public Long getUserId() {
        return usuarioActual != null ? usuarioActual.getId() : null;
    }
    
    public String getUserEmail() {
        return usuarioActual != null ? usuarioActual.getEmail() : null;
    }
    
    public String getUserNombreCompleto() {
        return usuarioActual != null ? usuarioActual.getNombreCompleto() : "Usuario";
    }
    
    public Integer getUserTipoId() {
        return usuarioActual != null ? usuarioActual.getTipoId() : null;
    }
}