package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Colmena {
    private final List<Child> ninosCapturados;
    private final ReentrantLock lock;
    private final Logger logger;
    
    public Colmena() {
        this.ninosCapturados = new ArrayList<>();
        this.lock = new ReentrantLock(true);
        this.logger = Logger.getInstance();
    }
    
    // Traer niño a la party
    public void entrar(Child nino) {
        lock.lock();
        try {
            ninosCapturados.add(nino);
            logger.log(nino.getNinoId() + " ha sido depositado en la Colmena");
            System.out.println(nino.getNinoId() + " ha sido depositado en la Colmena");
        } finally {
            lock.unlock();
        }
    }
    
    // Sacar un niño ej. Once (como el supermercado en argentina)
    public void salir(Child nino) {
        lock.lock();
        try {
            ninosCapturados.remove(nino);
            logger.log(nino.getNinoId() + " ha salido de la Colmena");
            System.out.println(nino.getNinoId() + " ha salido de la Colmena");
        } finally {
            lock.unlock();
        }
    }
    
    // Se explica bastante solo
    public boolean hayNinos() {
        lock.lock();
        try {
            return !ninosCapturados.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public Child seleccionarNinoAleatorio() {
        lock.lock();
        try {
            if (ninosCapturados.isEmpty()) return null;
            int index = (int) (Math.random() * ninosCapturados.size());
            return ninosCapturados.get(index);
        } finally {
            lock.unlock();
        }
    }
    
    // Cantidad de Prepubertos en la colmena
    public int getCantidadNinos() {
        lock.lock();
        try {
            return ninosCapturados.size();
        } finally {
            lock.unlock();
        }
    }
}