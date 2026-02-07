import {Injectable} from '@angular/core';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  error(message: string) {
    alert(message);
  }

  success(message: string) {
    console.log(message);
  }
}
