package org.landvibe.ass1.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Book {

    private Long id;
    private String title;

    public Book(String title) {
        this.title = title;
    }
}
