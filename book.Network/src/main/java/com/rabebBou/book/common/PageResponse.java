package com.rabebBou.book.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private  long TotalElements;
    private int totalPages;
    private  boolean first;
    private  boolean last;
}
