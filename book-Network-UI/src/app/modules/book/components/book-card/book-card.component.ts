import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from 'src/app/services/models';

@Component({
  selector: 'app-book-card',
  templateUrl: './book-card.component.html',
  styleUrls: ['./book-card.component.scss'],
})
export class BookCardComponent {
  private _book: BookResponse = {};
  private _bookCover: string | undefined;
  private _manage = false;

  //getter to manger
  get manage() {
    return this._manage;
  }
  //setter to manger
  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  //get book cover
  get bookCover(): string | undefined {
    if (this._book.cover) {
      return 'data:image/jpg;base64, ' + this._book.cover;
    }
    return 'https://source.unsplash.com/user/c_v_r/1900x800';
  }

  //getter
  get book(): BookResponse {
    return this._book;
  }
  //setter
  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  @Output() private share: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();
  @Output() private details: EventEmitter<BookResponse> =
    new EventEmitter<BookResponse>();

  onArchive(): void {
    this.archive.emit(this._book);
  }
  onBorrow(): void {
    this.borrow.emit(this._book);
  }
  onShowDetails(): void {
    this.details.emit(this._book);
  }
  onAddToWaitingList(): void {
    this.addToWaitingList.emit(this._book);
  }
  onEdit(): void {
    this.edit.emit(this._book);
  }
  onShare(): void {
    this.share.emit(this._book);
  }
}
