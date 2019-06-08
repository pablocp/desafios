package idwall.desafio.string;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdwallFormatterTest {

    @Test
    public void formatLineSmallerThanLimitShouuldntChangeIt() {
        StringFormatter sf = new IdwallFormatter(10, false);
        String text = "text";
        assertEquals (text, sf.format(text));
    }

    @Test
    public void shouldKeepOriginalLineBreaks() {
        StringFormatter sf = new IdwallFormatter(30, false);
        String text = "line1\nline2\nline3";
        assertEquals (text, sf.format(text));
    }

    @Test
    public void shouldBreakLargeLines() {
        StringFormatter sf = new IdwallFormatter(10, false);
        String text = "a text larger than the limit";
        String expected = "a text\nlarger\nthan the\nlimit";
        assertEquals (expected, sf.format(text));
    }

    @Test
    public void shouldKeepMultipleLineBreaks() {
        StringFormatter sf = new IdwallFormatter(10, false);
        String text = "line1\n\nline2";
        assertEquals (text, sf.format(text));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptWordsLargerThanLimit() {
        StringFormatter sf = new IdwallFormatter(10, false);
        sf.format("It's Supercalifragilisticexpialidocious!");
    }

    @Test
    public void shouldJustifyText() {
        StringFormatter sf = new IdwallFormatter(10, true);
        String text = "a text larger than the limit";
        String expected = "a   text\nlarger\nthan the\nlimit";
        assertEquals (expected, sf.format(text));
    }

    @Test
    public void shouldHandleEmptyText() {
        StringFormatter sf = new IdwallFormatter(10, true);
        String text = "";
        assertEquals (text, sf.format(text));
    }
}
