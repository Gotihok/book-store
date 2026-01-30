import {Component, OnInit, signal} from '@angular/core';
import {inject} from 'vitest';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../services/book.service';
import {BookResponse} from '../../api/book-response';

@Component({
  standalone: true,
  selector: 'app-book-details.page',
  imports: [],
  templateUrl: './book-details.page.html',
  styleUrl: './book-details.page.css',
})
export class BookDetailsPage implements OnInit {
  protected readonly book = signal<BookResponse | null>(null);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);
  private readonly apiUrlFromState: string | null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private bookService: BookService
  ) {
    const navigation = this.router.currentNavigation();
    this.apiUrlFromState = navigation?.extras.state?.['bookApiUrl'] ?? null;
  }

  ngOnInit(): void {
    console.log('ngOnInit() invoked', this.apiUrlFromState);

    if (this.apiUrlFromState) {
      console.log('apiUrlFromState not null');
      this.loadByUrl(this.apiUrlFromState);
      return;
    }

    const isbn = this.route.snapshot.paramMap.get('isbn');

    if (!isbn) {
      this.fail('Missing book identifier');
      return;
    }

    this.loadByIsbn(isbn);
  }

  private loadByUrl(url: string): void {
    console.log('loadByUrl() invoked');

    this.bookService.getBookByUrl(url).subscribe({
      next: (book: BookResponse) => {
        console.log('Book response: ', book);

        this.book.set(book);
        console.log('Book from field: ', book);

        this.isLoading.set(false);
        console.log('Is loading?: ', this.isLoading);
      },
      error: () => {
        console.log('Failed to load book');
        this.fail('Failed to load book')
      }
    });
  }

  private loadByIsbn(isbn: string): void {
    console.log('loadByIsbn() invoked');

    this.bookService.getBookByIsbn(isbn).subscribe({
      next: book => {
        this.book.set(book);
        this.isLoading.set(false);
      },
      error: () => this.fail('Book not found')
    });
  }

  private fail(message: string): void {
    console.log('fail() invoked');

    this.errorMessage.set(message);
    this.isLoading.set(false);
  }
}
