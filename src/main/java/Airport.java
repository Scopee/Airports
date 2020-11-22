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
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < args.length; i++) {
            try {
                fields[i].set(this, args[i]);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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
