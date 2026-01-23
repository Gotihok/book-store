import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {RegisterRequest} from '../api/register-request';
import {TokenResponse} from '../api/token-response';
import {LoginRequest} from '../api/login-request';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly EXPIRES_AT_KEY = 'expires_at';

  //TODO: make env config for base url
  private readonly apiUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(request: RegisterRequest): Observable<TokenResponse> {
    return this.http
      .post<TokenResponse>(`${this.apiUrl}/register`, request)
      .pipe(tap(res => this.storeToken(res)));
  }

  login(request: LoginRequest): Observable<TokenResponse> {
    return this.http
      .post<TokenResponse>(`${this.apiUrl}/login`, request)
      .pipe(tap(res => this.storeToken(res)));
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.EXPIRES_AT_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const expiresAt = localStorage.getItem(this.EXPIRES_AT_KEY);
    return !!expiresAt && Date.now() < Number(expiresAt);
  }

  private storeToken(tokenResponse: TokenResponse): void {
    localStorage.setItem(this.TOKEN_KEY, tokenResponse.token);

    const expiresAt = Date.now() + tokenResponse.expiresIn;
    localStorage.setItem(this.EXPIRES_AT_KEY, expiresAt.toString());
  }
}
