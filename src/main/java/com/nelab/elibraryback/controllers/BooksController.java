package com.nelab.elibraryback.controllers;

import com.nelab.elibraryback.dto.Book;
import com.nelab.elibraryback.services.BooksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

 @RestController @RequestMapping("books") @Slf4j
public class BooksController {

    @Autowired
    private final BooksService service;

    public BooksController(BooksService service) {
        this.service = service;
    }

    @GetMapping("getBooks")
    public ResponseEntity<Set<Book>> getBooks(){
        return new ResponseEntity<>(service.getBooks(),HttpStatus.OK);
    }


}
