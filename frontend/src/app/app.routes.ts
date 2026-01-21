import { Routes } from '@angular/router';
import { RegisterPage } from './features/auth/pages/register.page/register.page';

export const routes: Routes = [
  { path: 'register', component: RegisterPage },

  //TODO: change later to proper redirection
  { path: '', component: RegisterPage }
];
