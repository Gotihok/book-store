import { Routes } from '@angular/router';
import {AuthGuard} from './shared/http/auth.guard';

export const routes: Routes = [
  // AUTH
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/pages/login.page/login.page').then(m => m.LoginPage)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/pages/register.page/register.page').then(m => m.RegisterPage)
  },

  // TEST
  {
    path: 'users',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/test/pages/test.page/test.page').then(m => m.TestPage)
  },

  // BOOKS
  {
    path: 'books/create',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/books/pages/book-creation.page/book-creation.page').then(m => m.BookCreationPage)
  },
  {
    path: 'books',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/books/pages/books-search.page/books-search.page').then(m => m.BooksSearchPage)
  },
  {
    path: 'books/:isbn',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/books/pages/book-details.page/book-details.page').then(m => m.BookDetailsPage)
  },

  // FALLBACKS
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
