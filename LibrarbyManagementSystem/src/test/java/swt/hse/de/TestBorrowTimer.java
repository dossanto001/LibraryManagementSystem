package swt.hse.de;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestBorrowTimer {
    BorrowTimer bt;

    @Before
    public void setup() {
       bt = new BorrowTimer();
    }

    @Test
    public void testBorrowForTime() throws ParseException {
        Date d = new SimpleDateFormat( "MM-dd-yyyy" ).parse( "08-19-1975" );
        bt.setDate(d);
        String expected = "08-26-1975";
        String actual = bt.borrowForTime(7);

        Assert.assertEquals(expected, actual);
    }

}
