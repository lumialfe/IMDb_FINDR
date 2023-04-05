package org.imdb.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Starring {

    private Name name;
    private String characters;

}
