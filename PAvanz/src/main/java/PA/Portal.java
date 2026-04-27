package PA;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Portal {
    private final String nombre;
    private final int tamanoGrupo; //La verdad lo quisiera llamar size, :(
    private final Zona zonadestino;
    private final Sistemageneral sistema;

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
    }
    
    //
    public void esperarycruzar(Child nino) throws InterruptedException {
        lock.lock();
        try {
            // Barrera de Apagón
            while (sistema.hayApagon) {
                esperagrupo.await();
            }

            // Formación de grupo (Barrera de tamaño)
            ninosesperandoGrupo++;
            if (ninosesperandoGrupo < tamanoGrupo) {
                esperagrupo.await(); 
            } else {
                ninosesperandoGrupo = 0;
                esperagrupo.signalAll(); 
            }

            // Antes de cruzar, esperamos a que no haya NADIE regresando
            while (ninosregresando > 0) {
                esperaretorno.await();
            }
        } finally {
            lock.unlock();
        }

        // Cruce físico (Solo uno a la vez)
        pasounico.acquire(); 
        try {
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
            ninosregresando++;
        } finally {
            lock.unlock();
        }

        // 2. Cruce físico con prioridad
        pasounico.acquire();
        try {
            System.out.println(nino.getNinoId() + " REGRESA con prioridad por " + nombre);
            Thread.sleep(1000);
        } finally {
            pasounico.release();

            // Al terminar, restamos y avisamos a los que esperan para entrar
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
