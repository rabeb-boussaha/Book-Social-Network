package com.rabebBou.book.feedback;

import com.rabebBou.book.book.Book;
import com.rabebBou.book.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Feedback extends BaseEntity {
    @Column
    private Double note; // 1 a 5 stars
    private String comment;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


}
