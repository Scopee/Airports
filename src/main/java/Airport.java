import com.opencsv.bean.CsvBindByPosition;

import java.lang.reflect.Field;

public class Airport {
    private String id;
    private String name;
    private String city;
    private String country;
    private String iata;
    private String icao;
    private String latitude;
    private String longitude;
    private String altitude;
    private String timezone;
    private String DST;
    private String ianaTimeZone;
    private String type;
    private String source;

    public Airport() {
    }

    public Airport(String[] args) {
        this.id = args[0];
        this.name = args[1];
        this.city = args[2];
        this.country = args[3];
        this.iata = args[4];
        this.icao = args[5];
        this.latitude = args[6];
        this.longitude = args[7];
        this.altitude = args[8];
        this.timezone = args[9];
        this.DST = args[10];
        this.ianaTimeZone = args[11];
        this.type = args[12];
        this.source = args[13];
    }

    /**
     * Returns string representation of airport with indexed column in first place
     *
     * @param columnIndex indexed column
     * @return string representation
     */
    public String convertToString(int columnIndex) {
        StringBuilder sb = new StringBuilder();
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            sb.append(fields[columnIndex].get(this).toString()).append(" ");
            for (int i = 0; i < fields.length; i++) {
                if (i == columnIndex)
                    continue;
                sb.append(fields[i].get(this).toString()).append(" ");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
