import { Injectable } from '@angular/core';
import {BookCreationRequest} from '../api/book-creation-request';
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

  createBook(request: BookCreationRequest): Observable<HttpResponse<BookResponse>> {
    request = {
      ...request,
      isbn: request.isbn.replace(/[^0-9Xx]/g, '').toUpperCase()
    }

    return this.http.post<BookResponse>(
      `${this.apiUrl}/new`,
      request,
      { observe: 'response' },
    );
  }

  getBookByIsbn(isbn: string): Observable<BookResponse> {
    isbn = isbn.replace(/[^0-9Xx]/g, '').toUpperCase();
    return this.http.get<BookResponse>(`${this.apiUrl}/${isbn}`,);
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
}
