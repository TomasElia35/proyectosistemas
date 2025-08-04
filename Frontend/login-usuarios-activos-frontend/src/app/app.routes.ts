// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Components
import { LoginComponent } from './auth/components/login/login.component';

// Guards
import { AuthGuard } from './auth/guards/auth.guard';
import { AdminGuard } from './auth/guards/admin.guard';

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
    canActivate: [AuthGuard, AdminGuard] 
  },
  
  // Gestión de Usuarios (solo para administradores)
  {
    path: 'admin/usuarios',
    loadComponent: () => import('./auth/components/usuarios/user-list.component').then(c => c.UserListComponent),
    canActivate: [AuthGuard, AdminGuard]
  },
  {
  path: 'admin/activos',
  loadChildren: () => import('./features/activos/activos.module').then(m => m.ActivosModule),
  canActivate: [AuthGuard, AdminGuard]
  },

  {
    path: 'admin/usuarios/crear',
    loadComponent: () => import('./auth/components/usuarios/user-form.component').then(c => c.UserFormComponent),
    canActivate: [AuthGuard, AdminGuard]
  },
  {
    path: 'admin/usuarios/editar/:id',
    loadComponent: () => import('./auth/components/usuarios/user-form.component').then(c => c.UserFormComponent),
    canActivate: [AuthGuard, AdminGuard]
  },
  
  
  // Catch all - redirige a login si la ruta no existe
  { path: '**', redirectTo: '/login' }
];