import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LoggerService {
  private readonly STORAGE_KEY = 'app_logs';

  log(message: string) {
    const logs = this.getLogs();
    logs.push({
      message,
      timestamp: new Date().toISOString()
    });
    sessionStorage.setItem(this.STORAGE_KEY, JSON.stringify(logs));
  }

  getLogs(): any[] {
    const raw = sessionStorage.getItem(this.STORAGE_KEY);
    return raw ? JSON.parse(raw) : [];
  }

  clear() {
    sessionStorage.removeItem(this.STORAGE_KEY);
  }
}
