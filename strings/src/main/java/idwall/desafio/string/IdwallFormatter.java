package idwall.desafio.string;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IdwallFormatter implements StringFormatter {

    public IdwallFormatter() {
        this(40, false);
    }

    public IdwallFormatter(int lineWidthLimit, boolean justified) {
        this.setJustified(justified);
        this.setLimit(lineWidthLimit);
    }

    @Override
    public String format(String text) {
        List<LimitedTextLine> lines = this.breakTextLines(text)
                .flatMap(line -> cropLine(line))
                .collect(Collectors.toList());

        if (this.justified) {
            OptionalInt lineWidth = lines.stream()
                    .mapToInt(LimitedTextLine::getMinimumRequiredChars)
                    .max();
            if (!lineWidth.isPresent()) return "";

            return lines.parallelStream()
                    .map(line -> line.toJustifiedString(lineWidth.getAsInt()))
                    .collect(Collectors.joining("\n"));
        } else {
            return lines.parallelStream()
                    .map(LimitedTextLine::toString)
                    .collect(Collectors.joining("\n"));
        }
    }

    public boolean isJustified() {
        return justified;
    }

    public int getLimit() {
        return limit;
    }

    public void setJustified(boolean justified) {
        this.justified = justified;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private Stream<String> breakTextLines(String text) {
        return Arrays.stream(text.split("\\R"));
    }

    private Stream<LimitedTextLine> cropLine(String line) {
       Iterable<LimitedTextLine> iterable = () -> new LimitedTextLineIterator(line, this.limit);
       return StreamSupport.stream(iterable.spliterator(), true);
    }

    private boolean justified;
    private int limit;

}
