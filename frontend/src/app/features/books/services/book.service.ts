import { Injectable } from '@angular/core';
import {BookCreationRequest} from '../api/book-creation-request';
import {Observable} from 'rxjs';
import {BookResponse} from '../api/book-response';
import {HttpClient} from '@angular/common/http';
import {LoggerService} from '../../../shared/services/logger.service';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly apiUrl = 'http://localhost:8080/api/books';

  constructor(
    private http: HttpClient,
  ) {}

  createBook(request: BookCreationRequest): Observable<BookResponse> {
    return this.http.post<BookResponse>(`${this.apiUrl}/new`, request);
  }

  getBookByIsbn(isbn: string): Observable<BookResponse> {
    return new Observable();
  }

  findBooks(params: {
    author?: string;
    title?: string;
  }): Observable<BookResponse[]> {
    return new Observable();
  }
}
