package idwall.desafio.string;

import java.util.LinkedList;
import java.util.List;

public class LimitedTextLine {

    public LimitedTextLine(int limit) {
        this.limit  = limit;
        this.charCount = 0;
        this.words = new LinkedList<>();
    }

    public void addWord(String word) {
        this.words.add(word);
        this.charCount += word.length();
    }

    public boolean fits(String word) {
        return this.charCount + this.words.size() + word.length() <= this.limit;
    }

    public String toJustifiedString(int lineWidth) {
        if (this.words.isEmpty()) return "";
        if (this.words.size() == 1) return this.words.get(0);
        if (lineWidth < this.getMinimumRequiredChars())
            throw new IllegalArgumentException("Line width smaller than the minimum required.");

        int spaceWidth = (lineWidth - this.charCount)/(this.words.size() - 1);
        int numLargerSpaces = (lineWidth - this.charCount)%(this.words.size() - 1);

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for(String s : words) {
            if (!first) {
                for (int i = 0; i < spaceWidth; i++) {
                    sb.append(' ');
                }
                if (numLargerSpaces > 0) {
                    sb.append(' ');
                    numLargerSpaces--;
                }
            }
            first = false;
            sb.append(s);
        }

        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : words) {
            if (!first) {
                sb.append(' ');
            }
            first = false;

            sb.append(s);
        }
        return sb.toString();
    }

    public int getLimit() {
        return limit;
    }

    public int getMinimumRequiredChars() {
        if (this.words.isEmpty()) return 0;
        return this.charCount + this.words.size() - 1;
    }

    private int charCount;
    private int limit;
    private List<String> words;

}
