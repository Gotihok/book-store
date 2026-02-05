import {Component, OnInit, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../services/book.service';
import {BookResponse} from '../../api/book-response';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-book-details.page',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './book-details.page.html',
  styleUrl: './book-details.page.css',
})
export class BookDetailsPage implements OnInit {
  protected readonly book = signal<BookResponse | null>(null);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal<string | null>(null);
  protected readonly isEditing = signal(false);
  protected editForm: FormGroup;
  private readonly apiUrlFromState: string | null;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private bookService: BookService,
    private fb: FormBuilder
  ) {
    const navigation = this.router.currentNavigation();
    this.apiUrlFromState = navigation?.extras.state?.['bookApiUrl'] ?? null;
    this.editForm = this.fb.group({
      title: ['', Validators.required],
      author: ['', Validators.required],
      publisher: ['', Validators.required],
    });
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
        this.updateForm(book);
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
        this.updateForm(book);
        this.isLoading.set(false);
      },
      error: () => this.fail('Book not found')
    });
  }

  private updateForm(book: BookResponse): void {
    this.editForm.patchValue({
      title: book.title,
      author: book.author,
      publisher: book.publisher
    });
  }

  protected toggleEdit(): void {
    const currentBook = this.book();
    if (currentBook) {
      this.updateForm(currentBook);
    }
    this.isEditing.set(!this.isEditing());
  }

  protected deleteBook(): void {
    const currentBook = this.book();
    if (!currentBook) return;

    if (confirm(`Are you sure you want to delete "${currentBook.title}"?`)) {
      this.bookService.deleteBook(currentBook.isbn).subscribe({
        next: () => {
          this.router.navigate(['/books']);
        },
        error: (err) => {
          this.errorMessage.set('Failed to delete book');
          console.error('Delete error:', err);
        }
      });
    }
  }

  protected updateBook(): void {
    const currentBook = this.book();
    if (!currentBook || this.editForm.invalid) return;

    this.isLoading.set(true);
    this.bookService.updateBook(currentBook.isbn, this.editForm.value).subscribe({
      next: (updatedBook) => {
        this.book.set(updatedBook);
        this.isEditing.set(false);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Failed to update book');
        this.isLoading.set(false);
        console.error('Update error:', err);
      }
    });
  }

  private fail(message: string): void {
    console.log('fail() invoked');

    this.errorMessage.set(message);
    this.isLoading.set(false);
  }
}
