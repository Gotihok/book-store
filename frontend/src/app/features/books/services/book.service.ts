import { Injectable } from '@angular/core';
import {BookCreationRequest} from '../api/book-creation-request';
import {BookUpdateRequest} from '../api/book-update-request';
import {Observable} from 'rxjs';
import {BookResponse} from '../api/book-response';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {LoggerService} from '../../../shared/services/logger.service';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  private readonly apiUrl = 'http://localhost:8080/api/books';

  constructor(
    private http: HttpClient,
  ) {}

  private formatIsbn(isbn: string): string {
    return isbn.replace(/[^0-9Xx]/g, '').toUpperCase();
  }

  createBook(request: BookCreationRequest): Observable<HttpResponse<BookResponse>> {
    request = {
      ...request,
      isbn: this.formatIsbn(request.isbn)
    }

    return this.http.post<BookResponse>(
      `${this.apiUrl}/new`,
      request,
      { observe: 'response' },
    );
  }

  getBookByIsbn(isbn: string): Observable<BookResponse> {
    return this.http.get<BookResponse>(`${this.apiUrl}/${this.formatIsbn(isbn)}`,);
  }

  getBookByUrl(url: string): Observable<BookResponse> {
    return this.http.get<BookResponse>(url);
  }

  findBooks(params: {
    author?: string;
    title?: string;
  }): Observable<BookResponse[]> {
    let httpParams = new HttpParams();

    if (params.author) {
      httpParams = httpParams.set('author', params.author);
    }

    if (params.title) {
      httpParams = httpParams.set('title', params.title);
    }

    return this.http.get<BookResponse[]>(
      this.apiUrl,
      { params: httpParams }
    );
  }

  updateBook(isbn: string, request: BookUpdateRequest): Observable<BookResponse> {
    return this.http.patch<BookResponse>(`${this.apiUrl}/${this.formatIsbn(isbn)}`, request);
  }

  deleteBook(isbn: string): Observable<BookResponse> {
    return this.http.delete<BookResponse>(`${this.apiUrl}/${this.formatIsbn(isbn)}`);
  }
}
