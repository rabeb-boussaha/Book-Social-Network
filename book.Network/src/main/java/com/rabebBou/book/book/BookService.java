    package com.rabebBou.book.book;

    import com.rabebBou.book.common.PageResponse;
    import com.rabebBou.book.exception.OperationNotPermittedExecption;
    import com.rabebBou.book.file.FileStorageService;

    import com.rabebBou.book.history.BookTransactionHistory;
    import com.rabebBou.book.history.BookTransactionHistoryRepository;
    import com.rabebBou.book.user.User;
    import jakarta.persistence.EntityNotFoundException;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.domain.Sort;
    import org.springframework.security.core.Authentication;
    import org.springframework.stereotype.Service;
    import jakarta.persistence.*;
    import org.springframework.web.multipart.MultipartFile;

    import java.util.List;
    import java.util.Objects;

    import static com.rabebBou.book.book.BookSpecification.*;

    @Service
    @RequiredArgsConstructor
    @Slf4j
    public class BookService {
        private final BookRepository bookRepository;
        private final BookTransactionHistoryRepository transactionHistoryRepository;

        private final FileStorageService fileStorageService;
        private  final BookMapper bookMapper;
        public Integer save(BookRequest request, Authentication connectedUser) {
            User user = ((User) connectedUser.getPrincipal());
            Book book = bookMapper.toBook(request);
            book.setOwner(user);
            return bookRepository.save(book).getId();
        }



        public BookResponse findById(Integer bookId) {
            return bookRepository.findById(bookId)
                    .map(bookMapper::toBookResponse)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with ID:: " + bookId));
        }

        public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
            User user = ((User) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<Book>  books = bookRepository.findAllDisplayableBooks(pageable , user.getId());
            List<BookResponse> booksResponse = books.stream()
                    .map(bookMapper::toBookResponse)
                    .toList();
            return new PageResponse<>(
                    booksResponse,
                    books.getNumber(),
                    books.getSize(),
                    books.getTotalElements(),
                    books.getTotalPages(),
                    books.isFirst(),
                    books.isLast()
            );
        }

        public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
            User user = ((User) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<Book>  books = bookRepository.findAll(withOwnerId(user.getId()), pageable);
            List<BookResponse> booksResponse = books.stream()
                    .map(bookMapper::toBookResponse)
                    .toList();
            return new PageResponse<>(
                    booksResponse,
                    books.getNumber(),
                    books.getSize(),
                    books.getTotalElements(),
                    books.getTotalPages(),
                    books.isFirst(),
                    books.isLast()
            );
        }


        public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
            User user = ((User) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<BookTransactionHistory>  allBorrowedBooks = transactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
           List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                   .map(bookMapper::toBorrowedResponse)
                   .toList();
            return new PageResponse<>(
                    bookResponse,
                    allBorrowedBooks.getNumber(),
                    allBorrowedBooks.getSize(),
                    allBorrowedBooks.getTotalElements(),
                    allBorrowedBooks.getTotalPages(),
                    allBorrowedBooks.isFirst(),
                    allBorrowedBooks.isLast()
            );
        }

        public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
            User user = ((User) connectedUser.getPrincipal());
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
            Page<BookTransactionHistory>  allBorrowedBooks = transactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());
            List<BorrowedBookResponse> bookResponse = allBorrowedBooks.stream()
                    .map(bookMapper::toBorrowedResponse)
                    .toList();
            return new PageResponse<>(
                    bookResponse,
                    allBorrowedBooks.getNumber(),
                    allBorrowedBooks.getSize(),
                    allBorrowedBooks.getTotalElements(),
                    allBorrowedBooks.getTotalPages(),
                    allBorrowedBooks.isFirst(),
                    allBorrowedBooks.isLast()
            );
        }


        public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
            //fetch a book

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("No Book found with the ID:: " + bookId));
            // only the owner of the book can update the  book
            User user = ((User) connectedUser.getPrincipal());
            if(!Objects.equals(book.getOwner().getId(), user.getId())){
                throw new OperationNotPermittedExecption(" You cannnot update books shearble status");
            }
            book.setShareable(!book.isShareable());
            bookRepository.save(book);
            return bookId;
        }

        public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
            //fetch a book
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("No Book found with the ID:: " + bookId));
            // only the owner of the book can update the  book
            User user = ((User) connectedUser.getPrincipal());
            if(!Objects.equals(book.getOwner().getId(), user.getId())){
                throw new OperationNotPermittedExecption(" You cannot update others books archived status");
            }
            book.setArchived(!book.isArchived());
            bookRepository.save(book);
            return bookId;
        }

        public Integer borrowBook(Integer bookId, Authentication connectedUser) {
        // fetch a book c a d on verfifier si ona a book in database
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("Not found a book with ID ::" + bookId));
            // verfifier a book not archived and shearble
            if(book.isArchived()|| !book.isShareable()){
                throw new OperationNotPermittedExecption("The requested book cannot be borrowed since it is archived or not shareable");}
            //if a book is not archived and is shearble
            // 1 need a user object c ad user connect and user not a owner
          User user =((User) connectedUser.getPrincipal());
          if(Objects.equals(book.getOwner().getId(),user.getId())){
          throw  new OperationNotPermittedExecption("you cannot borrow your own book ");
          }
                // is the book is already borrowed or not
            final boolean isAlreadyBorrowedByUser = transactionHistoryRepository.isAlreadyBorrowedByUser(bookId, user.getId());
            if(isAlreadyBorrowedByUser){
                throw new OperationNotPermittedExecption("the requested book is already borrowed");
            }
            BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                    .user(user)
                    .book(book)
                    .returned(false)
                    .returnApproved(false)
                    .build();
            return transactionHistoryRepository.save(bookTransactionHistory).getId();
        }


        public Integer returnBorrowBook(Integer bookId, Authentication connectedUser) {
            // check a book in our database
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("not found a book with ID ::" + bookId));
    // verfifier a book not archived and shearble
            if(book.isArchived()|| !book.isShareable()){
                throw new OperationNotPermittedExecption("The requested book cannot be borrowed since it is archived or not shareable");}
            User user = ((User) connectedUser.getPrincipal());
            if(!Objects.equals(book.getOwner().getId(), user.getId())){
                throw new OperationNotPermittedExecption(" You cannot borrow or return  your owner  book");

            }
             BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndUserId(bookId, user.getId())
                     .orElseThrow(()-> new OperationNotPermittedExecption("You did not borrow this book "));

            bookTransactionHistory.setReturned(true);
            return transactionHistoryRepository.save(bookTransactionHistory).getId();



        }

        public Integer approveReturnBorrowBook(Integer bookId, Authentication connectedUser ) {
            // check a book in our database
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("not found a book with ID ::" + bookId));
    // verfifier a book not archived and shearble
            if(book.isArchived()|| !book.isShareable()){
                throw new OperationNotPermittedExecption("The requested book cannot be borrowed since it is archived or not shareable");}
            User user = ((User) connectedUser.getPrincipal());
            if(!Objects.equals(book.getOwner().getId(), user.getId())){
                throw new OperationNotPermittedExecption(" You cannot borrow or return  your owner  book");
            }
            //need find a book  by the owner
            BookTransactionHistory bookTransactionHistory = transactionHistoryRepository.findByBookIdAndOwnerId(bookId, user.getId())
                    .orElseThrow(()-> new OperationNotPermittedExecption("the book is not returned yet . you cannot approve its returned "));
            bookTransactionHistory.setReturnApproved(true);
            return  transactionHistoryRepository.save(bookTransactionHistory).getId();




        }

        public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUser, Integer bookId) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(()-> new EntityNotFoundException("not found a book with ID ::" + bookId));

            User user = ((User) connectedUser.getPrincipal());
            var bookCover = fileStorageService.saveFile(file, bookId, user.getId());
            book.setBookCover(bookCover);
            bookRepository.save(book);
        }
    }

