package PA;

public class Demogorgon extends Thread{
    private final String id;
    private final Sistemageneral sistema;
    private Zona zonaActual;
    private int contadorCapturas;
    
    public Demogorgon(String id, Sistemageneral sistema) {
        this.id = id;
        this.sistema = sistema;
        this.contadorCapturas = 0;
        Zona[] zonasUD = {
            sistema.bosque,
            sistema.laboratorio,
            sistema.centroComercial,
            sistema.alcantarillado
        }; // Hacer ruleta para ver donde empiezan a spawniar
        int index = (int) (Math.random() * zonasUD.length);
        this.zonaActual = zonasUD[index];
    }
    
    // Getters :-)
    public String getDemogorgonId() {
        return id;
    }
    
    public int getContadorCapturas() {
        return contadorCapturas;
    }
    
    public Zona getZonaActual() {
        return zonaActual;
    }
    
    public void incrementarCapturas() {
        this.contadorCapturas++;
    }
    
    @Override
    public void run() {
        // Insertar comportamiento.exe
        while (true) {
            try {
                // Lógica temporal.png
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
