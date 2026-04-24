package PA.Modelo;

// FOREST, LAB, MALL, SEWER, HIVE, MAIN_STREET, BYERS_BASEMENT, RADIO_WSQK

public enum ZoneType {
    // Hawkins zones (zona segura)
    MAIN_STREET,
    BYERS_BASEMENT,
    RADIO_WSQK,
    
    // Upside Down zones (zonas inseguras)
    FOREST,
    LAB,
    MALL,
    SEWER,
    
    // Hive (colmena - almacén de víctimas)
    HIVE,
    
    // Portals (para tracking, no son zonas físicas donde habitan entidades)
    PORTAL_FOREST,
    PORTAL_LAB,
    PORTAL_MALL,
    PORTAL_SEWER;
    
    // Método útil para saber si una zona es del Upside Down
    public boolean isUpsideDownZone() {
        return this == FOREST || this == LAB || this == MALL || this == SEWER;
    }
    
    // Método útil para saber si es una zona segura (Hawkins)
    public boolean isHawkinsZone() {
        return this == MAIN_STREET || this == BYERS_BASEMENT || this == RADIO_WSQK;
    }
}