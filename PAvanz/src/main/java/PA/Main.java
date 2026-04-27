package PA;


public class Main {
    public static void main(String[] args) {
        Sistemageneral sistema = new Sistemageneral();
        
        System.out.println("La simulación comienza");
        sistema.iniciar(); 
        sistema.lanzar_ciclo_eventos();
        
        
    }
}