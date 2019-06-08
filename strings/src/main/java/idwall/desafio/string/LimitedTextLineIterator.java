package idwall.desafio.string;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class LimitedTextLineIterator implements Iterator<LimitedTextLine> {

    public LimitedTextLineIterator(String textLine, int limit) {
        this.words = new LinkedList<String>(Arrays.asList(textLine.split("\\s")));
        this.limit = limit;
    }

    @Override
    public boolean hasNext() {
        return !words.isEmpty();
    }

    @Override
    public LimitedTextLine next() {
        LimitedTextLine line = new LimitedTextLine(limit);

        if (words.getFirst().length() > limit) {
            throw new IllegalArgumentException("Word larger than maximum line length: " + words.getFirst());
        }

        while(!words.isEmpty() && line.fits(words.getFirst())) {
            line.addWord(words.poll());
        }
        return line;
    }

    private LinkedList<String> words;
    private int limit;

}

