package com.nelab.elibraryback.services;

import com.nelab.elibraryback.dto.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component @Slf4j
public class BooksService {

    public Set<Book> getBooks(){
        log.info("getting books");
        Set<Book> books = new HashSet<>();
        books.add(Book.builder()
                .author("Lewis Carroll")
                .id("ISBN 1616402237")
                .title("Alice's Adventures in Wonderland")
                .type("Fantasy")
                .year("1865")
                .build());
        books.add(Book.builder()
                .author("Allah")
                .id("ISBN 00000000")
                .title("Al9or2an Al kareem")
                .type("The truth")
                .year("-13")
                .build());
        books.add(Book.builder()
                .author("George Orwell")
                .id("ISBN 0547249640")
                .title("1983")
                .type("Sci-Fi")
                .year("1865")
                .build());
        log.info(String.format("%d book(s) found",books.size()));
        return books;
    }
}
