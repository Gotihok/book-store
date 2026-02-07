import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {NavItem, TopNavComponent} from './shared/components/top-nav.component/top-nav.component';
import {AuthService} from './features/auth/services/auth.service';

//TODO: implement page specific title at the header
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopNavComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(
    private router: Router,
    private authService: AuthService,
  ) {}

  nav: NavItem[] = [
    { label: 'Search', route: '/books' },
    { label: 'Create', route: '/books/create' },
    { label: 'Test', route: '/users' }
  ];

  isAuthenticated(): boolean {
    return this.authService.isAuthenticated();
  }

  protected loginRedirect() {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
