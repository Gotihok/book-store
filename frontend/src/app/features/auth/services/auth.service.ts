import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {RegisterRequest} from '../models/register-request';
import {RegisterResponse} from '../models/register-response';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly apiUrl = '/api/auth'; // or environment.apiUrl

  constructor(private http: HttpClient) {}

  register(payload: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(
      'http://localhost:8080/api/auth/register',
      // '${this.apiUrl}/register',
      payload
    );
  }
}
