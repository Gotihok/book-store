import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AuthService} from '../../../auth/services/auth.service';
import {LoggerService} from '../../../../shared/services/logger.service';

@Component({
  selector: 'app-test.page',
  imports: [],
  templateUrl: './test.page.html',
  styleUrl: './test.page.css',
})
export class TestPage {
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private logger: LoggerService
  ) {
    this.http.get('http://localhost:8080/api/users')
      .subscribe({
        next: () => console.log('JWT valid', authService.getToken()),
        error: (err) => console.log('Unauthorized', authService.getToken(), err)
      });
    console.log(this.logger.getLogs());
  }
}
