import {Component, computed, OnInit, signal} from '@angular/core';
import {BookResponse} from '../../api/book-response';
import {FormControl, FormGroup, NonNullableFormBuilder, ReactiveFormsModule} from '@angular/forms';
import {BookService} from '../../services/book.service';
import {Router} from '@angular/router';
import {FormFieldComponent} from '../../../../shared/components/form-field.component/form-field.component';

export type BookSearchFormModel = {
  title: FormControl<string>;
  author: FormControl<string>;
};

type SortOption =
  | 'TITLE_ASC'
  | 'TITLE_DESC'
  | 'AUTHOR_ASC'
  | 'AUTHOR_DESC';

//TODO: frontend move sorting to backend
@Component({
  selector: 'app-books-search.page',
  imports: [
    ReactiveFormsModule,
    FormFieldComponent
  ],
  templateUrl: './books-search.page.html',
  styleUrl: './books-search.page.css',
})
export class BooksSearchPage implements OnInit {
  protected searchForm: FormGroup<BookSearchFormModel>;

  protected books = signal<BookResponse[]>([]);
  protected isLoading = signal(false);
  protected hasSearched = signal(false);

  protected sortOption = signal<SortOption>('TITLE_ASC');

  constructor(
    private fb: NonNullableFormBuilder,
    private bookService: BookService,
    private router: Router,
  ) {
    this.searchForm = this.fb.group({
      title: [''],
      author: [''],
    });
  }

  ngOnInit() {
    console.log('ngOnInit() called');
    this.isLoading.set(true);
    this.search();
  }

  protected sortedBooks = computed((): BookResponse[] => {
    const books = [...this.books()];
    const sort = this.sortOption();

    return books.sort((a, b) => {
      switch (sort) {
        case 'TITLE_ASC':
          return a.title.localeCompare(b.title);
        case 'TITLE_DESC':
          return b.title.localeCompare(a.title);
        case 'AUTHOR_ASC':
          return a.author.localeCompare(b.author);
        case 'AUTHOR_DESC':
          return b.author.localeCompare(a.author);
        default:
          return 0;
      }
    });
  });

  protected onSearch(): void {
    const { title, author } = this.searchForm.getRawValue();
    this.isLoading.set(true);
    this.search(title, author);
  }

  protected clear(): void {
    this.searchForm.reset();
    this.books.set([]);
    this.hasSearched.set(false);
    this.search();
  }

  protected openBook(book: BookResponse): void {
    this.router.navigate(['/books', book.isbn]);
  }

  protected changeSort(option: SortOption): void {
    this.sortOption.set(option);
  }

  private search(title?: string, author?: string): void {
    console.log('Search params', {title, author});

    this.bookService.findBooks({ title, author }).subscribe({
      next: (books) => {
        this.books.set(books);
        this.isLoading.set(false);
        this.hasSearched.set(true);
      },
      error: (err) => {
        console.error(err);
        this.isLoading.set(false);
        this.hasSearched.set(true);
      }
    });
  }
}
