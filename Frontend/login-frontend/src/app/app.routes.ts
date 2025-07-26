// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Components - Rutas corregidas según tu estructura real
import { LoginComponent } from './auth/components/login/login.component';

// Guards - Rutas corregidas según tu estructura real
import { AuthGuard } from './auth/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  // Dashboard general (para usuarios normales)
  { 
    path: 'dashboard', 
    loadComponent: () => import('./features/dashboard/components/dashboard/dashboard.component').then(c => c.DashboardComponent),
    canActivate: [AuthGuard] 
  },
  
  // Admin Dashboard
  { 
    path: 'admin/dashboard', 
    loadComponent: () => import('./features/dashboard/components/dashboard/admin-dashboard.component').then(c => c.AdminDashboardComponent),
    canActivate: [AuthGuard] 
  },
  
  // Catch all - redirige a login si la ruta no existe
  { path: '**', redirectTo: '/login' }
];