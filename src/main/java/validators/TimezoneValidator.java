package validators;

import java.time.DateTimeException;
import java.time.ZoneId;

public class TimezoneValidator {
    public static boolean isValidTimezone(String timezone){
        try{
            ZoneId.of(timezone);
            return true;
        }
        catch (DateTimeException ex){
            return false;
        }
    }
}
