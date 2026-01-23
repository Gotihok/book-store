import { Component } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-test.page',
  imports: [],
  templateUrl: './test.page.html',
  styleUrl: './test.page.css',
})
export class TestPage {
  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/api/users')
      .subscribe({
        next: () => console.log('JWT valid'),
        error: () => console.log('Unauthorized')
      });
  }
}
