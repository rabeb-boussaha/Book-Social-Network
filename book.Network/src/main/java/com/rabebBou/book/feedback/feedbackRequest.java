package com.rabebBou.book.feedback;

import jakarta.validation.constraints.*;

public record feedbackRequest(
        @Positive(message = "200")
        @Min(value = 0, message = "201")
        @Max(value = 5, message = "202")
        @NotNull(message = "note cannot be null")
        Double note,
        @NotNull(message = "203")
        @NotEmpty(message = "203")
        @NotBlank(message = "203")
        @NotNull(message = "comment cannot be null")
        String comment,
        @NotNull(message = "204")
        Integer bookId

) {
}
