import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  NonNullableFormBuilder,
  ReactiveFormsModule,
  Validators
} from "@angular/forms";
import {FormFieldComponent} from '../../../../shared/components/form-field.component/form-field.component';
import {AuthService} from '../../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import {LoggerService} from '../../../../shared/services/logger.service';
import {LoginRequest} from '../../api/login-request';
import {RegisterRequest} from '../../api/register-request';

export type LoginFormModel = {
  username: FormControl<string>;
  password: FormControl<string>;
};

@Component({
  selector: 'app-login.page',
  imports: [
    FormsModule,
    FormFieldComponent,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './login.page.html',
  styleUrl: './login.page.css',
})
export class LoginPage {
  protected loginForm: FormGroup<LoginFormModel>;
  protected isSubmitting: boolean = false;

  constructor(
    private fb: NonNullableFormBuilder,
    private authService: AuthService,
    private router: Router,
    private logger: LoggerService
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  protected onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;

    const payload: LoginRequest = this.loginForm.getRawValue();

    this.authService.login(payload).subscribe({
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
