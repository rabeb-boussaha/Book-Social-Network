package com.rabebBou.book.book;

import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopis;
    private  String owner;
    private  byte[] cover;
    private double rate;
    private boolean archived;
    private boolean shareable;
}
