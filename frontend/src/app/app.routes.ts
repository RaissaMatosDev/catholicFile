import { Routes } from '@angular/router';
import { authGuard } from './auth/guards/auth-guard';
import { noAuthGuard } from './auth/guards/no-auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'cadastro',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./auth/cadastro/cadastro.component').then(m => m.CadastroComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () => import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'folhetos',
    canActivate: [authGuard],
    loadComponent: () => import('./folhetos/folhetos').then(m => m.Folhetos)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }];
