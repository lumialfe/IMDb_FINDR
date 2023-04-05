package org.imdb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Akas {
    private String title;
    private String region;
    private String language;
    private boolean isOriginal;

}
