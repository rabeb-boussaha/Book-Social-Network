import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class TokenService {
  set token(token: string) {
    localStorage.setItem('token', token);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  clearToken(): void {
    localStorage.removeItem('token');
  }

  updateToken(newToken: string): void {
    this.clearToken();
    this.token = newToken;
  }
}
