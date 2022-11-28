package swt.hse.de;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class BorrowTimer {
    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

    Calendar cal;


    public String borrowForTime(int days){
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        return sdf.format(cal.getTime());
    }

    public void setDate(Date date){
        cal.setTime(date);
    }




}
