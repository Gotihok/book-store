import { Component } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {NavItem, TopNavComponent} from './shared/components/top-nav.component/top-nav.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, TopNavComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  constructor(
    private router: Router,
  ) {}

  nav: NavItem[] = [
    { label: 'Search', route: '/books' },
    { label: 'Create', route: '/books/create' },
    { label: 'Test', route: '/users' }
  ];

  protected loginRedirect() {
    this.router.navigate(['/login']);
  }
}
