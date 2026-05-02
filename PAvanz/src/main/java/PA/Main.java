package PA;


public class Main {
    public static void main(String[] args) {
        Sistemageneral sistema = new Sistemageneral();
        try {
            ServidorSockets servidor = new ServidorSockets(sistema, 8080);
            servidor.iniciar();
        } catch (java.io.IOException e) {
            System.err.println("Error al iniciar el servidor socket: " + e.getMessage());
            System.err.println("El sistema continuará ejecutándose sin acceso remoto.");
        }
        
        Logger.getInstance().log("=== LA BATALLA YA HA EMPEZADO ===");
        System.out.println("La simulación comienza");
        
        java.awt.EventQueue.invokeLater(() -> {
            GUI gui = new GUI(sistema);
            gui.setVisible(true);
        });
        
        sistema.iniciar(); 
        sistema.lanzar_ciclo_eventos();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Logger.getInstance().log("=== SIMULACIÓN FINALIZADA ===");
            Logger.getInstance().cerrar();
        }));
    }
}