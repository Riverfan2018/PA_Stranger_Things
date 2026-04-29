package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Portal {
    private final String nombre;
    private final int tamanoGrupo;
    private final Zona zonadestino;
    private final Sistemageneral sistema;
    private final Logger logger;
    
    private int ninosesperandoGrupo = 0;
    
    private final List<Child> ninosEsperandoSalida;
    private final List<Child> ninosEsperandoRegreso;
   
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
        this.ninosEsperandoSalida = new ArrayList<>();
        this.ninosEsperandoRegreso = new ArrayList<>();
    }
    
    public void esperarycruzar(Child nino) throws InterruptedException {
        lock.lock();
        try {
            // Barrera de Apagón
            while (sistema.hayApagon) {
                esperagrupo.await();
            }
            
            // Esperar a que no haya niños regresando (prioridad a regreso)
            while (ninosregresando > 0) {
                esperaretorno.await();
            }

            // Formación de grupo (Barrera de tamaño)
            ninosEsperandoSalida.add(nino);
            ninosesperandoGrupo++;
            logger.log(nino.getNinoId() + " espera grupo en portal " + nombre + " (" + ninosesperandoGrupo + "/" + tamanoGrupo + ")");
            System.out.println(nino.getNinoId() + " espera grupo en portal " + nombre + " (" + ninosesperandoGrupo + "/" + tamanoGrupo + ")");
            
            if (ninosesperandoGrupo < tamanoGrupo) {
                esperagrupo.await(); 
            } else {
                // Grupo completo - limpiar lista de salida
                ninosEsperandoSalida.clear();
                ninosesperandoGrupo = 0;
                esperagrupo.signalAll(); 
            }
            
            // Antes de cruzar, esperamos a que no haya NADIE regresando
            while (ninosregresando > 0) {
                esperaretorno.await();
            }
            
        } catch (Exception e) {
            logger.log("Error en portal " + nombre + ": " + e.getMessage());
            System.out.println("Something went wrong.");
        } finally {
            lock.unlock();
        }

        // Cruce físico (Solo uno a la vez)
        pasounico.acquire(); 
        try {
            logger.log(nino.getNinoId() + " está cruzando el portal " + nombre + " → UPSIDE DOWN");
            System.out.println(nino.getNinoId() + " cruzando al Upside Down por " + nombre);
            Thread.sleep(1000); 
        } finally {
            pasounico.release();
        }
    }

    public void regresaraHawkins(Child nino) throws InterruptedException {
        //Marcamos que hay alguien queriendo volver (bloquea a los que quieren entrar)
        lock.lock();
        try {
            ninosEsperandoRegreso.add(nino);
            ninosregresando++;
            logger.log(nino.getNinoId() + " espera REGRESO en portal " + nombre);
        } finally {
            lock.unlock();
        }

        //Cruce físico con prioridad
        pasounico.acquire();
        try {
            logger.log(nino.getNinoId() + " regresa a Hawkins por el portal " + nombre);
            System.out.println(nino.getNinoId() + " REGRESA con prioridad por " + nombre);
            Thread.sleep(1000);
        } finally {
            pasounico.release();

            // Al terminar, restamos y avisamos a los que esperan para entrar
            lock.lock();
            try {
                ninosEsperandoRegreso.remove(nino);
                ninosregresando--;
                if (ninosregresando == 0) {
                    esperaretorno.signalAll(); 
                }
            } finally {
                lock.unlock();
            }
        }
    }
    
    // ========== GETTERS PARA LA GUI ==========
    public List<Child> getNinosEsperandoSalida() {
        lock.lock();
        try {
            return new ArrayList<>(ninosEsperandoSalida);
        } finally {
            lock.unlock();
        }
    }
    
    public List<Child> getNinosEsperandoRegreso() {
        lock.lock();
        try {
            return new ArrayList<>(ninosEsperandoRegreso);
        } finally {
            lock.unlock();
        }
    }
    
    public int getCantidadEsperandoSalida() {
        lock.lock();
        try {
            return ninosEsperandoSalida.size();
        } finally {
            lock.unlock();
        }
    }
    
    public int getCantidadEsperandoRegreso() {
        lock.lock();
        try {
            return ninosEsperandoRegreso.size();
        } finally {
            lock.unlock();
        }
    }
    
    public void limpiarListaSalida() {
        lock.lock();
        try {
            ninosEsperandoSalida.clear();
        } finally {
            lock.unlock();
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
