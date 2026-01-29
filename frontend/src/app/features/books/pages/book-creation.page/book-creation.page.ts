import { Component } from '@angular/core';
import {
  FormBuilder,
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

    const raw = this.bookCreationForm.getRawValue();

    const payload: BookCreationRequest = {
      ...raw,
      isbn: raw.isbn.replace(/[^0-9Xx]/g, '').toUpperCase()
    }

    console.log(payload);

    this.bookService.createBook(payload).subscribe({
      next: (response: BookResponse) => {
        console.log(response);
        // //TODO: make proper navigation
        // this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }
}
