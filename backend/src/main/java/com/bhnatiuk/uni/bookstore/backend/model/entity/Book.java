package com.bhnatiuk.uni.bookstore.backend.model.entity;

import com.bhnatiuk.uni.bookstore.backend.model.domain.Isbn;
import com.bhnatiuk.uni.bookstore.backend.model.entity.converter.IsbnConverter;
import jakarta.persistence.*;
import lombok.Data;

//TODO: add nullable description
//TODO: add book's content with polyglot persistence
@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            unique = true,
            nullable = false,
            length = 13
    )
    @Convert(converter = IsbnConverter.class)
    private Isbn isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;
}
