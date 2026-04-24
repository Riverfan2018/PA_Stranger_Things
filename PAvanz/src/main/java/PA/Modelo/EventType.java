package PA.Modelo;

// Eventos como POWER_OUTAGE, UPSIDE_DOWN_STORM, ELEVEN_INTERVENTION, MIND_FLAYER

public enum EventType {
    POWER_OUTAGE,        // Apagón del laboratorio: portales inutilizables
    UPSIDE_DOWN_STORM,   // Tormenta: tiempo de recolección x2, ataques más rápidos
    ELEVEN_INTERVENTION, // Intervención de Eleven: libera niños de la colmena
    MIND_FLAYER;         // Red mental: demogorgons van a zona con más niños
    
    @Override
    public String toString() {
        return switch (this) {
            case POWER_OUTAGE -> "APAGÓN DEL LABORATORIO";
            case UPSIDE_DOWN_STORM -> "TORMENTA DEL UPSIDE DOWN";
            case ELEVEN_INTERVENTION -> "INTERVENCIÓN DE ELEVEN";
            case MIND_FLAYER -> "LA RED MENTAL";
        };
    }
}
