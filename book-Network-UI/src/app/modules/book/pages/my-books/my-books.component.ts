import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageResponseBookResponse } from 'src/app/services/models';
import { BookResponse } from '../../../../services/models/book-response';
import { BookService } from 'src/app/services/services';

@Component({
  selector: 'app-my-books',
  templateUrl: './my-books.component.html',
  styleUrls: ['./my-books.component.scss'],
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 4;

  constructor(private router: Router, private bookService: BookService) {}

  ngOnInit(): void {
    this.findAllBooks();
  }

  private findAllBooks(): void {
    this.bookService
      .findAllBooksByOwner({
        page: this.page,
        size: this.size,
      })
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
        },
      });
  }

  goToFirstPage(): void {
    this.page = 0;
    this.findAllBooks();
  }
  goToPreviousPage(): void {
    this.page--;
    this.findAllBooks();
  }
  gotToPage(page: number): void {
    this.page = page;
    this.findAllBooks();
  }
  goToNextPage(): void {
    this.page++;
    this.findAllBooks();
  }
  goToLastPage(): void {
    this.page = (this.bookResponse.totalPages as number) - 1;
    this.findAllBooks();
  }

  get isLastpage(): boolean {
    return this.page == (this.bookResponse.totalPages as number) - 1;
  }

  archiveBook(book: BookResponse): void {
    this.bookService
      .updateArchivedStatus({
        'book-id': book.id as number,
      })
      .subscribe({
        next: () => {
          book.archived = !book.archived;
        },
      });
  }

  shareBook(book: BookResponse): void {
    this.bookService
      .updateShareableStatus({
        'book-id': book.id as number,
      })
      .subscribe({
        next: () => {
          book.shareable = !book.shareable;
        },
      });
  }
  editBook(book: BookResponse): void {
    this.router.navigate(['books', 'manage', book.id]);
  }
}
