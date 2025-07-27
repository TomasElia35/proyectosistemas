import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    const currentUser = this.authService.getCurrentUser();
    
    console.log('🔍 AdminGuard - Usuario actual:', currentUser);
    console.log('🔍 AdminGuard - Rol del usuario:', currentUser?.rol?.nombre);
    
    if (currentUser && currentUser.rol) {
      const rolUsuario = currentUser.rol.nombre.toUpperCase();
      
      // Permitir acceso a administradores
      if (rolUsuario === 'ADMINISTRADOR') {
        console.log('✅ AdminGuard - Acceso permitido (ADMINISTRADOR)');
        return true;
      }
      
      // Opcionalmente, también puedes permitir acceso a técnicos a ciertas rutas admin
      // if (rolUsuario === 'TECNICO') {
      //   console.log('✅ AdminGuard - Acceso permitido (TECNICO)');
      //   return true;
      // }
    }
    
    console.log('❌ AdminGuard - Acceso denegado, redirigiendo a dashboard');
    // Si no es admin, redirigir al dashboard normal
    this.router.navigate(['/dashboard']);
    return false;
  }
}