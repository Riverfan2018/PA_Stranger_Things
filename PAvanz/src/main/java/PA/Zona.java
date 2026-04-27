package PA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Zona {
    private final List<Child> ninosPresentes;
    private final ReentrantLock lock;
    
    public enum TipoZona {
        CALLE_PRINCIPAL,
        SOTANO_BYERS,
        RADIO_WSQK,
        BOSQUE,
        LABORATORIO,
        CENTRO_COMERCIAL,
        ALCANTARILLADO;
        
    }
    
    private final TipoZona tipo;
    
    // Constructor
    public Zona(TipoZona tipo) {
        this.tipo = tipo;
        this.ninosPresentes = new ArrayList<>();
        this.lock = new ReentrantLock(true);
    }
    
    // Funcion niño entrar isla???
    public void entrar(Child nino) {
        lock.lock();
        try {
            ninosPresentes.add(nino);
            System.out.println(nino.getNinoId() + " ha entrado a " + tipo);
        } finally {
            lock.unlock();
        }
    }
    
    // Funcion niño escapar isla
    public void salir(Child nino) {
        lock.lock();
        try {
            ninosPresentes.remove(nino);
            System.out.println(nino.getNinoId() + " ha salido de " + tipo);
        } finally {
            lock.unlock();
        }
    }
    
    // Detector de niños, no usar ilegalmente
    public boolean hayNinos() {
        lock.lock();
        try {
            return !ninosPresentes.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    // Para que Daddy Demogorgon se lleve un niño ramdom
    public Child seleccionarNinoAleatorio() {
        lock.lock();
        try {
            if (ninosPresentes.isEmpty()) return null;
            int index = (int) (Math.random() * ninosPresentes.size());
            return ninosPresentes.get(index);
        } finally {
            lock.unlock();
        }
    }
    
    public Child capturarnino(Child nino) {
        lock.lock();
        try {
            if (ninosPresentes.remove(nino)) {
                System.out.println(nino.getNinoId() + " ha sido capturado de " + tipo);
                return nino;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }
    
    // Getter
    public TipoZona getTipo() {
        return tipo;
    }
    
    // Para la GUI cuando necesite una copia de la lista de niños por zona
    public List<Child> getNinosPresentes() {
        lock.lock();
        try {
            return new ArrayList<>(ninosPresentes);
        } finally {
            lock.unlock();
        }
    }
    
    // Cuantos niños en la isla
    public int getCantidadNinos() {
        lock.lock();
        try {
            return ninosPresentes.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean esZonaHawkins() {
    return tipo == TipoZona.CALLE_PRINCIPAL || 
           tipo == TipoZona.SOTANO_BYERS || 
           tipo == TipoZona.RADIO_WSQK;
    }

    public boolean esZonaUpsideDown() {
        return tipo == TipoZona.BOSQUE || 
               tipo == TipoZona.LABORATORIO ||
               tipo == TipoZona.CENTRO_COMERCIAL || 
               tipo == TipoZona.ALCANTARILLADO;
    }
}
