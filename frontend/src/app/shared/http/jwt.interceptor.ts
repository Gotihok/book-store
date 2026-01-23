import {Injectable} from '@angular/core';
import {HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {AuthService} from '../../features/auth/services/auth.service';
import {catchError, throwError} from 'rxjs';
import {Router} from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();

    if (!token) {
      return next.handle(req);
    }

    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next.handle(authReq).pipe(
      catchError(err => {
        if (err.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
        }
        return throwError(() => err);
      })
    );
  }
}
