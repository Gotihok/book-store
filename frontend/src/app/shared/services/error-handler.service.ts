import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {NotificationService} from './notification.service';

@Injectable({ providedIn: 'root' })
export class ErrorHandlerService {

  constructor(
    private router: Router,
    private notify: NotificationService
  ) {}

  handle(error: HttpErrorResponse): void {
    switch (error.status) {
      case 0:
        this.handleNetworkError();
        break;

      case 400:
        this.handleBadRequest(error);
        break;

      case 401:
        this.handleUnauthorized();
        break;

      case 403:
        this.handleForbidden();
        break;

      case 404:
        this.handleNotFound();
        break;

      case 409:
        this.handleConflict(error);
        break;

      case 422:
        this.handleValidation(error);
        break;

      case 500:
      default:
        this.handleServerError();
    }
  }

  private handleNetworkError() {
    this.notify.error('Server unreachable. Check your connection.');
  }

  private handleBadRequest(error: HttpErrorResponse) {
    this.notify.error(error.error?.message || 'Invalid request');
  }

  private handleUnauthorized() {
    this.notify.error('Unauthorized. Please log in.');
    this.router.navigate(['/login']);
  }

  private handleForbidden() {
    this.notify.error('Access denied');
  }

  private handleNotFound() {
    this.notify.error('Resource not found');
  }

  private handleConflict(error: HttpErrorResponse) {
    this.notify.error(error.error?.message || 'Conflict occurred');
  }

  private handleValidation(error: HttpErrorResponse) {
    this.notify.error('Validation failed');
  }

  private handleServerError() {
    this.notify.error('Unexpected server error');
  }
}
