package model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Article {

    private Set<aTag> tags;

    private String header;

    private List<Paragraph> paragraphs;

    @Data
    private class Paragraph {
        private List<String> paragraphElements;
    }
}
