package PA.Util;

public class Constantes {
    // Constantes (puerto, tiempos, rutas)
    
    // Configuración Sockets
    public static final int SERVER_PORT = 9999;
    public static final int CLIENT_REFRESH_MS = 1000; // 1 segundo
    
    // Configuración Eventos
    public static final int EVENT_INTERVAL_MIN = 30000; // 30 segundos
    public static final int EVENT_INTERVAL_MAX = 60000; // 60 segundos
    public static final int EVENT_DURATION_MIN = 5000;  // 5 segundos
    public static final int EVENT_DURATION_MAX = 10000; // 10 segundos
    
    // Configuración Child o Niños
    public static final int TOTAL_CHILDREN = 1500;
    public static final int CHILD_CREATION_MIN_MS = 500;   // 0.5 segundos
    public static final int CHILD_CREATION_MAX_MS = 2000;  // 2 segundos
    
    // Conportamientos de Child
    public static final int BYERS_BASEMENT_MIN_MS = 1000;   // 1 segundo
    public static final int BYERS_BASEMENT_MAX_MS = 2000;   // 2 segundos
    public static final int UPSIDE_DOWN_MIN_MS = 3000;      // 3 segundos
    public static final int UPSIDE_DOWN_MAX_MS = 5000;      // 5 segundos
    public static final int RADIO_WSQK_MIN_MS = 2000;       // 2 segundos
    public static final int RADIO_WSQK_MAX_MS = 4000;       // 4 segundos
    public static final int MAIN_STREET_MIN_MS = 3000;      // 3 segundos
    public static final int MAIN_STREET_MAX_MS = 5000;      // 5 segundos
    
    // Tamaño de los Grupos en Portales
    public static final int PORTAL_FOREST_SIZE = 2;
    public static final int PORTAL_LAB_SIZE = 3;
    public static final int PORTAL_MALL_SIZE = 4;
    public static final int PORTAL_SEWER_SIZE = 2;
    public static final long PORTAL_CROSSING_TIME_MS = 1000; // 1 segundo
    
    // Configuración Demogorgon
    public static final int DEMOGORGON_INITIAL_ID = 0; // D0000
    public static final int CAPTURES_FOR_NEW_DEMO = 8;
    
    // Configuración Ataque
    public static final int ATTACK_DURATION_MIN_MS = 500;   // 0.5 segundos
    public static final int ATTACK_DURATION_MAX_MS = 1500;  // 1.5 segundos
    public static final double RESIST_PROBABILITY = 2.0 / 3.0; // 66.67%
    
    // Demogorgon idle (Sin Niños en Zona)
    public static final int IDLE_MIN_MS = 4000;  // 4 segundos
    public static final int IDLE_MAX_MS = 5000;  // 5 segundos
    
    // Hive deposit time
    public static final int HIVE_DEPOSIT_MIN_MS = 500;  // 0.5 segundos
    public static final int HIVE_DEPOSIT_MAX_MS = 1000; // 1 segundo
}
