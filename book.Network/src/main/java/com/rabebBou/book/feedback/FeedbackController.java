package com.rabebBou.book.feedback;

import com.rabebBou.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
@Tag(name="Feedback")
public class FeedbackController {
    private final FeedbackService service;
    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody feedbackRequest request,
            Authentication connectedUser
    ){
        return  ResponseEntity.ok(service.save(request, connectedUser));
    }

    @GetMapping("/book/{book_id}")
    public ResponseEntity<PageResponse<FeedBackResponse>> FindAllFeedbackByBook(
            @PathVariable("book-id") Integer bookId,
            @RequestParam(name="page", defaultValue = "0", required = false) int page,
            @RequestParam(name="size", defaultValue = "0", required = false) int size,
            Authentication connectedUser

    ){
        return ResponseEntity.ok(service.findAllFeedbacksByBook(bookId, page, size, connectedUser));
    }



}
