package PA;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Portal {
    private final String nombre;
    private final int tamanoGrupo; //La verdad lo quisiera llamar size, :(
    private final Zona zonadestino;
    private final Sistemageneral sistema;
    private final Logger logger;
    
    private int ninosesperandoGrupo = 0;
   
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition esperagrupo = lock.newCondition();
    private final Condition esperaretorno = lock.newCondition();
    
    
    private final Semaphore pasounico = new Semaphore(1, true);
    
   
    private int ninosregresando = 0;

    // Constructor
    public Portal(String nombre, int tamanogrupo, Zona destino, Sistemageneral sistema) {
        this.nombre = nombre;
        this.tamanoGrupo = tamanogrupo;
        this.zonadestino = destino;
        this.sistema = sistema;
        this.logger = Logger.getInstance();
    }
    
    //
    public void esperarycruzar(Child nino) throws InterruptedException {

        lock.lock();
        try {
            
            while (sistema.hayApagon) {
                esperagrupo.await();
            }

            
            ninosesperandoGrupo++;
            logger.log(nino.getNinoId() + " espera grupo en portal " + nombre + " (" + ninosesperandoGrupo + "/" + tamanoGrupo + ")");
            System.out.println(nino.getNinoId() + " espera grupo en portal " + nombre + " (" + ninosesperandoGrupo + "/" + tamanoGrupo + ")");
            
            if (ninosesperandoGrupo < tamanoGrupo) {
                esperagrupo.await(); 
            } else {
                ninosesperandoGrupo = 0;
                esperagrupo.signalAll(); 
            }

            
            while (ninosregresando > 0) {
                esperaretorno.await();
            }
        } catch (Exception e) {
            logger.log("Error en portal " + nombre + ": " + e.getMessage());
            System.out.println("Something went wrong.");
        } finally {
            lock.unlock(); // Si ocurre una excepción el Lock se queda lockeado
        }

       
        pasounico.acquire(); 
        try {
            logger.log(nino.getNinoId() + " está cruzando el portal " + nombre);
            System.out.println(nino.getNinoId() + " está cruzando el portal " + nombre);
            Thread.sleep(1000); 
        } finally {
            pasounico.release();
        }
    }

    public void regresaraHawkins(Child nino) throws InterruptedException {
        lock.lock();
        try {
            ninosregresando++;
        } finally {
            lock.unlock();
        }

        
        pasounico.acquire();
        try {
            logger.log(nino.getNinoId() + " regresa a Hawkins por el portal " + nombre);
            System.out.println(nino.getNinoId() + " regresa a Hawkins por el portal " + nombre);
            Thread.sleep(1000);
        } finally {
            pasounico.release();
            
            lock.lock();
            try {
                ninosregresando--;
                if (ninosregresando == 0) {
                    esperaretorno.signalAll(); 
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    public Zona getZonaDestino() {
        return zonadestino;
    }
    
    public void despertarPorLuz() {
    lock.lock();
    try {
        esperagrupo.signalAll();
    } finally {
        lock.unlock();
    }
}
}
