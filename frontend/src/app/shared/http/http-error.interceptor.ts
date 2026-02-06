import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {ErrorHandlerService} from '../services/error-handler.service';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private errorHandler: ErrorHandlerService) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        this.errorHandler.handle(error);
        return throwError(() => error);
      })
    );
  }
}
