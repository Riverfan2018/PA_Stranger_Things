package PA;


public class Main {
    public static void main(String[] args) {
        Sistemageneral sistema = new Sistemageneral();
        
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