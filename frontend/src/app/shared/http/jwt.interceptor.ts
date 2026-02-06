import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {AuthService} from '../../features/auth/services/auth.service';
import {catchError, Observable, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {LoggerService} from '../services/logger.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private logger: LoggerService
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    this.logger.log("Token got by interceptor: " + token);

    if (!token || req.url.includes('/auth')) {
      this.logger.log("Token null or auth endpoint in JwtInterceptor");
      return next.handle(req);
    }

    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next.handle(authReq);
  }
}
