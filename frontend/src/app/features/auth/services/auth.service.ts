import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Observable, tap} from 'rxjs';
import {RegisterRequest} from '../api/register-request';
import {TokenResponse} from '../api/token-response';
import {LoginRequest} from '../api/login-request';
import {LoggerService} from '../../../shared/services/logger.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly TOKEN_KEY = 'access_token';
  private readonly EXPIRES_AT_KEY = 'expires_at';

  //TODO: make env config for base url
  private readonly apiUrl = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private logger: LoggerService
  ) {}

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
    sessionStorage.removeItem(this.TOKEN_KEY);
    sessionStorage.removeItem(this.EXPIRES_AT_KEY);
  }

  getToken(): string | null {
    this.logger.log("Retrieving token form AuthService: " + sessionStorage.getItem(this.TOKEN_KEY));
    return sessionStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    const expiresAt = sessionStorage.getItem(this.EXPIRES_AT_KEY);
    return !!expiresAt && Date.now() < Number(expiresAt);
  }

  private storeToken(tokenResponse: TokenResponse): void {
    this.logger.log("Response passed for saving: " + tokenResponse);
    this.logger.log("Saving token in AuthService: " + tokenResponse.jwtToken);
    sessionStorage.setItem(this.TOKEN_KEY, tokenResponse.jwtToken);

    const expiresAt = Date.now() + tokenResponse.expiresIn;
    sessionStorage.setItem(this.EXPIRES_AT_KEY, expiresAt.toString());
  }
}
