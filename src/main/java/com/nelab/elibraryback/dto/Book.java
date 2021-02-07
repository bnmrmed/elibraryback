package com.nelab.elibraryback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Book {
    private String id;
    private String title;
    private String author;
    private String year;
    private String type;

}
