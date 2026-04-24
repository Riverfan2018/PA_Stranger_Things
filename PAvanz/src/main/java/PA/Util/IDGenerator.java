package PA.Util;

public class IDGenerator {
    // Generador de IDs (N0001, D0001, etc.)
    
    private static int lastChildNumber = 0;
    private static int lastDemoNumber = 0;
    
    // Genera ID para niño: N0001, N0002, etc.
    public static synchronized String generateChildId(int sequentialNumber) {
        lastChildNumber = sequentialNumber;
        return String.format("N%04d", sequentialNumber);
    }
    
    // Genera ID para demogorgon: D0001, D0002, etc. (D0000 es Alpha)
    public static synchronized String generateDemogorgonId(int idNumber) {
        lastDemoNumber = idNumber;
        return String.format("D%04d", idNumber);
    }
    
    // Para obtener el siguiente número de niño (sin generar el string)
    public static synchronized int getNextChildNumber() {
        return lastChildNumber + 1;
    }
    
    // Para obtener el siguiente número de demogorgon
    public static synchronized int getNextDemoNumber() {
        return lastDemoNumber + 1;
    }
}
