package com.rabebBou.book.book;

import com.rabebBou.book.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name="Book")
public class BookController {



    private final BookService service;
    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(service.save(request, connectedUser));
    }
// methode de  findbyId
    @GetMapping("/{book-id}")
    public ResponseEntity<BookResponse>findBookById(
            @PathVariable("book-id") Integer bookId
    ){
          return  ResponseEntity.ok(service.findById(bookId));
    }
 // methode de find all books
    @GetMapping
     public ResponseEntity<PageResponse<BookResponse>>findAllBooks(
             @RequestParam(name ="page", defaultValue = "0" , required = false) int page,
             @RequestParam(name ="size", defaultValue = "10" , required = false) int size,
              Authentication connectedUser

     ){
        return ResponseEntity.ok(service.findAllBooks(page, size,  connectedUser));
     }

   @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name ="page", defaultValue = "0" , required = false) int page,
            @RequestParam(name ="size", defaultValue = "10" , required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name ="page", defaultValue = "0" , required = false) int page,
            @RequestParam(name ="size", defaultValue = "10" , required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllBorrowedBooks(page, size, connectedUser));
    }


    @GetMapping("/retunerd")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name ="page", defaultValue = "0" , required = false) int page,
            @RequestParam(name ="size", defaultValue = "10" , required = false) int size,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.findAllReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("/shareable/{book-id}")
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser));
    }

    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchivedStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.updateArchivedStatus(bookId, connectedUser));
    }

    // implmenet borrow book feature ( c a d comme ona va louer le book )
     @PostMapping("/borrow/{book-id}")
    public  ResponseEntity<Integer> borrowBook(
             @PathVariable("book-id") Integer bookId,
             Authentication connectedUser
             ){
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUser));
     }
// aprés la location de book comment je recupere ce book
    @PatchMapping("/borrow/return/{book-id}")
     public ResponseEntity<Integer> returnBorrowBook(
             @PathVariable("book-id") Integer bookId,
             Authentication connectedUser
    ){
        return ResponseEntity.ok(service.returnBorrowBook(bookId, connectedUser));
    }
    // approve return borrow book
    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnBorrowBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.approveReturnBorrowBook(bookId, connectedUser));
    }
// uplode a file not dans our data base mais dans le file systheme ou our server
@PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
public ResponseEntity<?> uploadBookCoverPicture(
        @PathVariable("book-id") Integer bookId,
        @Parameter()
        @RequestPart("file") MultipartFile file,
        Authentication connectedUser
) {
    service.uploadBookCoverPicture(file, connectedUser, bookId);
    return ResponseEntity.accepted().build();
}



}


