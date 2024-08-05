import { Component, OnInit } from '@angular/core';
import { returnBorrowBook } from 'src/app/services/fn/book/return-borrow-book';
import {
  BorrowedBookResponse,
  FeedbackRequest,
  FeedBackResponse,
  PageResponseBorrowedBookResponse,
} from 'src/app/services/models';
import { BookService } from 'src/app/services/services';

@Component({
  selector: 'app-borrowed-book-list',
  templateUrl: './borrowed-book-list.component.html',
  styleUrls: ['./borrowed-book-list.component.scss'],
})
export class BorrowedBookListComponent implements OnInit {
  borrowedBooks: PageResponseBorrowedBookResponse = {};
  selectedBook: BorrowedBookResponse | undefined = undefined;
  feedbackRequest: FeedbackRequest = { bookId: 0, comment: '', note: 0 };
  page = 0;
  size = 5;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.finsAllBorrowedBooks();
  }

  private finsAllBorrowedBooks() {
    this.bookService
      .findAllBorrowedBooks({
        page: this.page,
        size: this.size,
      })
      .subscribe({
        next: (resp) => {
          this.borrowedBooks = resp;
        },
      });
  }

  goToFirstPage(): void {
    this.page = 0;
    this.finsAllBorrowedBooks();
  }
  goToPreviousPage(): void {
    this.page--;
    this.finsAllBorrowedBooks();
  }
  gotToPage(page: number): void {
    this.page = page;
    this.finsAllBorrowedBooks();
  }
  goToNextPage(): void {
    this.page++;
    this.finsAllBorrowedBooks();
  }
  goToLastPage(): void {
    this.page = (this.borrowedBooks.totalPages as number) - 1;
    this.finsAllBorrowedBooks();
  }
  get isLastpage(): boolean {
    return this.page == (this.borrowedBooks.totalPages as number) - 1;
  }

  returnBorrowedBook(book: BorrowedBookResponse) {
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;
  }
  returnBook(withFeedback: boolean) {}
}
