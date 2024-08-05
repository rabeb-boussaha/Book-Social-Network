import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  BookResponse,
  PageResponseBookResponse,
} from 'src/app/services/models';
import { BookService } from 'src/app/services/services';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss'],
})
export class BookListComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 4;
  message = '';
  level: 'success' | 'error' = 'success';

  constructor(private router: Router, private bookService: BookService) {}

  ngOnInit(): void {
    this.findAllBooks();
  }
  private findAllBooks(): void {
    this.bookService
      .findAllBooks({
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

  borrowBook(book: BookResponse): void {
    this.message = '';
    this.bookService
      .borrowBook({
        'book-id': book.id as number,
      })
      .subscribe({
        next: () => {
          this.level = 'error';
          this.message = 'Book successuffly added to your List';
        },
        error: (err) => {
          console.log(err);
          this.level = 'error';
          this.message = err.error.error;
        },
      });
  }
}
