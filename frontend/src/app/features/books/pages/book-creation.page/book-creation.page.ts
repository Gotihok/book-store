import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import {FormFieldComponent} from '../../../../shared/components/form-field.component/form-field.component';
import {BookCreationRequest} from '../../api/book-creation-request';
import {BookService} from '../../services/book.service';
import {Router} from '@angular/router';
import {BookResponse} from '../../api/book-response';
import {HttpResponse} from '@angular/common/http';

export type BookCreationFromModel = {
  isbn: FormControl<string>;
  title: FormControl<string>;
  author: FormControl<string>;
  publisher: FormControl<string>;
}

@Component({
  selector: 'app-book-creation.page',
  imports: [
    FormFieldComponent,
    ReactiveFormsModule
  ],
  templateUrl: './book-creation.page.html',
  styleUrl: './book-creation.page.css',
})
export class BookCreationPage {
  protected bookCreationForm: FormGroup<BookCreationFromModel>;
  protected isSubmitting: boolean = false;

  constructor(
    private fb: NonNullableFormBuilder,
    private bookService: BookService,
    private router: Router,
  ) {
    this.bookCreationForm = this.fb.group({
      isbn: ['', [Validators.required, Validators.minLength(10)]],
      title: ['', Validators.required],
      author: ['', Validators.required],
      publisher:['', Validators.required],
    });
  }

  protected onSubmit(): void {
    if (this.bookCreationForm.invalid) {
      this.bookCreationForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    const payload: BookCreationRequest = this.bookCreationForm.getRawValue();

    console.log(payload);

    this.bookService.createBook(payload).subscribe({
      next: (response: HttpResponse<BookResponse>): void => {
        console.log('Response: ', response);

        const book = response.body!;
        const location = response.headers.get('Location');
        console.log('Location: ', location);

        this.router.navigate(
          ['/books', book.isbn],
          {
            state: {bookApiUrl: location}
          }
        );
      },
      error: (err): void => {
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }
}
