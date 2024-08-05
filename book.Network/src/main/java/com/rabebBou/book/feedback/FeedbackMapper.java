package com.rabebBou.book.feedback;

import com.rabebBou.book.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {
    public Feedback toFeedback(feedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false) // not required has not impact :: just to satisfy lombok
                        .shareable(false) // not required has not impact :: just to satisfy lombok
                        .build()
                )
                .build();
    }


    public FeedBackResponse toFeedBackResponse(Feedback feedBack, Integer id) {
        return FeedBackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedback(Objects.equals(feedBack.getCreatedBy(), id))
                .build() ;
    }


}
