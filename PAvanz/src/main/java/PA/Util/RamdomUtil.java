package PA.Util;
import java.util.Random;

public class RamdomUtil {
    // Métodos estáticos para tiempos aleatorios
    
    private static final Random random = new Random();
    
    // Devuelve un número aleatorio entre min (inclusive) y max (inclusive)
    public static int randomInt(int min, int max) {
        if (min == max) return min;
        return min + random.nextInt(max - min + 1);
    }
    
    // Devuelve un número aleatorio entre min (inclusive) y max (inclusive) en milisegundos
    public static long randomLong(long min, long max) {
        if (min == max) return min;
        return min + (long)(random.nextDouble() * (max - min + 1));
    }
    
    // Devuelve true con una probabilidad dada (ej: 0.66 para 66%)
    public static boolean probability(double probability) {
        return random.nextDouble() < probability;
    }

}
