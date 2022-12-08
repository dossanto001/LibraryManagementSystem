package swt.hse.de;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TestPattern {
    private PatternChecking pt;

    @Before
    public void setup(){
        pt = new PatternChecking();
    }
    @Test
    public void testAlphanumbericTrue(){
        assertTrue(pt.checkAlphaNumeric("q89q"));
    }

    @Test
    public void testAlphanumericFalse(){
        assertFalse(pt.checkAlphaNumeric("paod324!"));
    }

    @Test
    public void testFloatingNumberTrue(){
        assertTrue(pt.checkNumber("324142.0"));
    }

    @Test
    public void testNumberTrue(){
        assertTrue(pt.checkNumber("324142"));
    }

    @Test
    public void testNumberFalse(){
        assertFalse(pt.checkAlphaNumeric("paod324!"));
    }

    @Test
    public void testYearTrue(){
        assertTrue(pt.checkYear("3241"));
    }

    @Test
    public void testYearFalse(){
        assertFalse(pt.checkYear("79054"));
    }

    @Test
    public void testWordTrue(){
        assertTrue(pt.checkWord("adafas"));
    }

    @Test
    public void testWordFalse(){
        assertFalse(pt.checkWord("79054adsf"));
    }

}
