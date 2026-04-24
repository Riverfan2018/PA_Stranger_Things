/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PA;

/**
 *
 * @author cesar
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Portal {
    private final String nombre;
    private final int tamanogrupo;
    private final Zona zonadestino;
    private final Sistemageneral sistema;

    private int ninosesperandoGrupo = 0;
   
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition esperagrupo = lock.newCondition();
    private final Condition esperaretorno = lock.newCondition();
    
    
    private final Semaphore pasounico = new Semaphore(1, true);
    
   
    private int ninosregresando = 0;

    public Portal(String nombre, int tamanogrupo, Zona destino, Sistemageneral sistema) {
        this.nombre = nombre;
        this.tamanogrupo = tamanogrupo;
        this.zonadestino = destino;
        this.sistema = sistema;
    }

       
    public void esperarycruzar(Child nino) throws InterruptedException {

        lock.lock();
        try {
            
            while (sistema.hayapagon) {
                esperagrupo.await(); 
            }

            
            ninosesperandoGrupo++;
           System.out.println(nino.getnombreID() + " espera grupo en portal " + nombre + " (" + ninosesperandoGrupo + "/" + tamanoGrupo + ")");
            
            if (ninosesperandoGrupo < tamanogrupo) {
                esperagrupo.await(); 
            } else {
                ninosesperandoGrupo = 0;
                esperagrupo.signalAll(); 
            }

            
            while (ninosregresando > 0) {
                esperaretorno.await();
            }
        } finally {
            lock.unlock();
        }

       
        pasounico.acquire(); 
        try {
            System.out.println(nino.getnombreID() + " está cruzando el portal " + nombre);
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
            System.out.println(nino.getnombreID() + " regresa a Hawkins por el portal " + nombre);
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
}
