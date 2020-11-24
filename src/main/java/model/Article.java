package model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class Article {

    private Set<aTag> tags;

    private String header;

    private List<String> paragraphs;

}
