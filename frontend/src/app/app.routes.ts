import { Routes } from '@angular/router';
import { RegisterPage } from './features/auth/pages/register.page/register.page';
import {AuthGuard} from './shared/http/auth.guard';

export const routes: Routes = [
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
  {
    path: 'users',
    canActivate: [AuthGuard],
    loadComponent: () =>
      import('./features/test/pages/test.page/test.page').then(m => m.TestPage)
  },
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
