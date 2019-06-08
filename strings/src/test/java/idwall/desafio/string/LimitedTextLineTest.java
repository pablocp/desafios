package idwall.desafio.string;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LimitedTextLineTest {

    @Test
    public void shouldAcceptSmallWord() {
        LimitedTextLine line = new LimitedTextLine(10);
        
        assertTrue(line.fits("hello"));
    }

    @Test
    public void shoulNotdAcceptLargeWord() {
        LimitedTextLine line = new LimitedTextLine(10);
        
        assertFalse(line.fits("supercalifragilisticexpialidocious"));
    }

    @Test
    public void shouldUpdateMinimumRequiredChars() {
        LimitedTextLine line = new LimitedTextLine(10);
        
        assertEquals(0, line.getMinimumRequiredChars());
        
        line.addWord("hi");
        assertEquals(2, line.getMinimumRequiredChars());

        line.addWord("hello");
        assertEquals(8, line.getMinimumRequiredChars());
    }

    @Test
    public void shouldSeparateWordsBySpaces() {
        LimitedTextLine line = new LimitedTextLine(10);
        
        line.addWord("w1");
        line.addWord("w2");
        line.addWord("w3");

        assertEquals("w1 w2 w3", line.toString());
    }

    @Test
    public void shouldJustifyText() {
        LimitedTextLine line = new LimitedTextLine(15);
        
        line.addWord("w1");
        line.addWord("w2");
        line.addWord("w3");

        assertEquals("w1   w2  w3", line.toJustifiedString(11));
    }

    @Test(expected = IllegalArgumentException.class) 
    public void shouldNotJustifyWithSmallLineWidth() {
        LimitedTextLine line = new LimitedTextLine(15);
        
        line.addWord("w1");
        line.addWord("w2");
        line.addWord("w3");

        line.toJustifiedString(7);
    }
}
