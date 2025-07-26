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
    path: 'admin/usuarios/crear',
    loadComponent: () => import('./auth/components/usuarios/user-form.component').then(c => c.UserFormComponent),
    canActivate: [AuthGuard, AdminGuard]
  },
  {
    path: 'admin/usuarios/editar/:id',
    loadComponent: () => import('./auth/components/usuarios/user-form.component').then(c => c.UserFormComponent),
    canActivate: [AuthGuard, AdminGuard]
  },
  
  // Gestión de Insumos (requieren autenticación, algunos solo admin)
  {
    path: 'admin/insumos',
    loadComponent: () => import('./auth/components/insumos/isumo-list.component').then(c => c.InsumoListComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/insumos/crear',
    loadComponent: () => import('./auth/components/insumos/isumo-form.component').then(c => c.InsumoFormComponent),
    canActivate: [AuthGuard]
  },
  {
    path: 'admin/insumos/editar/:id',
    loadComponent: () => import('./auth/components/insumos/isumo-form.component').then(c => c.InsumoFormComponent),
    canActivate: [AuthGuard]
  },
  
  // Rutas de insumos para usuarios normales (solo lectura y actualización de stock)
  {
    path: 'insumos',
    loadComponent: () => import('./auth/components/insumos/isumo-list.component').then(c => c.InsumoListComponent),
    canActivate: [AuthGuard]
  },
  
  // Catch all - redirige a login si la ruta no existe
  { path: '**', redirectTo: '/login' }
];