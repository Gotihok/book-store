import { Component } from '@angular/core';
import {
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormControl,
  NonNullableFormBuilder,
} from '@angular/forms';
import {FormFieldComponent} from '../../../../shared/components/form-field.component/form-field.component';
import {RegisterRequest} from '../../api/register-request';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';

export type RegisterFormModel = {
  username: FormControl<string>;
  email: FormControl<string>;
  password: FormControl<string>;
  confirmPassword: FormControl<string>;
  terms: FormControl<boolean>;
};

@Component({
  selector: 'app-register.page',
  standalone: true,
  imports: [ReactiveFormsModule, FormFieldComponent, FormFieldComponent],
  templateUrl: './register.page.html',
  styleUrl: './register.page.css',
})
export class RegisterPage {
  protected registerForm: FormGroup<RegisterFormModel>;
  protected isSubmitting = false;

  constructor(
    private fb: NonNullableFormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      terms: [false, Validators.requiredTrue],
    });
  }

  protected onSubmit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    // Prepare payload for backend
    const { username, email, password } = this.registerForm.getRawValue() as {
      username: string;
      email: string;
      password: string;
    };

    const payload: RegisterRequest = { username, email, password };

    // Call backend via AuthService
    this.authService.register(payload).subscribe({
      next: () => {
        this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }
}
