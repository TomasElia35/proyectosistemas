// src/app/app.routes.ts
import { Routes } from '@angular/router';

// Components
import { LoginComponent } from './auth/login/login.component';
import { AdminDashboardComponent } from './admin/dashboard/admin-dashboard.component';
import { UserDashboardComponent } from './user/dashboard/user-dashboard.component';
import { ListaUsuariosComponent } from './admin/usuarios/lista-usuarios/lista-usuarios.component';
import { CrearUsuarioComponent } from './admin/usuarios/crear-usuario/crear-usuario.component';

// Guards
import { AuthGuard } from './auth/auth.guard';
import { AdminGuard } from './auth/admin.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  
  // Admin Routes
  { 
    path: 'admin/dashboard', 
    component: AdminDashboardComponent, 
    canActivate: [AuthGuard, AdminGuard] 
  },
  { 
    path: 'admin/usuarios', 
    component: ListaUsuariosComponent, 
    canActivate: [AuthGuard, AdminGuard] 
  },
  { 
    path: 'admin/usuarios/crear', 
    component: CrearUsuarioComponent, 
    canActivate: [AuthGuard, AdminGuard] 
  },
  
  // User Routes
  { 
    path: 'user/dashboard', 
    component: UserDashboardComponent, 
    canActivate: [AuthGuard] 
  },
  
  // Catch all
  { path: '**', redirectTo: '/login' }
];