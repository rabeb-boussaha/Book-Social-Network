package com.rabebBou.book.feedback;

import com.rabebBou.book.book.Book;
import com.rabebBou.book.book.BookRepository;
import com.rabebBou.book.common.PageResponse;
import com.rabebBou.book.exception.OperationNotPermittedExecption;
import com.rabebBou.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final BookRepository bookRepository;
    private final FeedbackMapper feedbackMapper;
    private final FeedBackRepository feedBackRepository;

    public Integer save(feedbackRequest request, Authentication connectedUser) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + request.bookId()));     if(book.isArchived()|| !book.isShareable()){
            throw new OperationNotPermittedExecption("you cannot give a feedback for an archived or not shareable book ");
        }
        User user = ((User) connectedUser.getPrincipal());
        if (Objects.equals(book.getOwner().getId(), user.getId())) {
            throw new OperationNotPermittedExecption("You cannot give feedback to your own book");
        }
        Feedback feedBack = feedbackMapper.toFeedback(request);
         return feedBackRepository.save(feedBack).getId();
    }


    @Transactional
    public PageResponse<FeedBackResponse> findAllFeedbacksByBook(Integer bookId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size);
        User user = ((User) connectedUser.getPrincipal());
        Page<Feedback> feedbacks = feedBackRepository.findAllByBookId(bookId, pageable);
        List<FeedBackResponse> feedbackResponses = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedBackResponse(f, user.getId()))
                .toList();
        return new PageResponse<>(
                feedbackResponses,
                feedbacks.getNumber(),
                feedbacks.getSize(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                feedbacks.isFirst(),
                feedbacks.isLast()
        );

    }
}
