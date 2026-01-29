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
import {Router, RouterLink} from '@angular/router';
import {LoggerService} from '../../../../shared/services/logger.service';

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
  imports: [ReactiveFormsModule, FormFieldComponent, FormFieldComponent, RouterLink],
  templateUrl: './register.page.html',
  styleUrl: './register.page.css',
})
export class RegisterPage {
  protected registerForm: FormGroup<RegisterFormModel>;
  protected isSubmitting: boolean = false;

  constructor(
    private fb: NonNullableFormBuilder,
    private authService: AuthService,
    private router: Router,
    private logger: LoggerService
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

    const payload: RegisterRequest = this.registerForm.getRawValue();
    // as {
    //   username: string;
    //   email: string;
    //   password: string;
    // };

    // Call backend via AuthService
    this.authService.register(payload).subscribe({
      next: () => {
        //TODO: make proper navigation
        this.router.navigate(['/users']);
      },
      error: (err) => {
        console.error(err);
        this.isSubmitting = false;
      }
    });
  }
}
