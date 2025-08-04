// import { Injectable } from '@angular/core';
// import { CanActivate, Router } from '@angular/router';
// import { AuthService } from '../services/auth.service';

// @Injectable({
//   providedIn: 'root'
// })
// export class AdminGuard implements CanActivate {

//   constructor(
//     private authService: AuthService,
//     private router: Router
//   ) {}

//   canActivate(): boolean {
//     console.log('%c🔍 ADMIN GUARD EJECUTÁNDOSE', 'color: orange; font-weight: bold');
    
//     const currentUser = this.authService.getCurrentUser();
//     const token = this.authService.getToken();
    
//     console.log('👤 Usuario actual:', currentUser);
//     console.log('🔐 Token presente:', !!token);
//     console.log('🎭 Rol del usuario:', currentUser?.rol?.nombre);
    
//     // Verificar autenticación básica
//     if (!token || !currentUser) {
//       console.log('❌ Sin token o usuario, redirigiendo a login');
//       this.router.navigate(['/login']);
//       return false;
//     }
    
//     // Verificar rol de administrador
//     if (currentUser && currentUser.rol) {
//       const rolUsuario = currentUser.rol.nombre.toUpperCase();
//       console.log('🔍 Rol normalizado:', rolUsuario);
      
//       if (rolUsuario === 'ADMINISTRADOR') {
//         console.log('✅ AdminGuard - Acceso permitido (ADMINISTRADOR)');
//         return true;
//       }
//     }
    
//     console.log('❌ AdminGuard - Acceso denegado, redirigiendo a dashboard');
//     this.router.navigate(['/dashboard']);
//     return false;
//   }
// }

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
    console.log('%c🔍 ADMIN GUARD EJECUTÁNDOSE', 'color: orange; font-weight: bold');
    
    const currentUser = this.authService.getCurrentUser();
    const token = this.authService.getToken();
    
    console.log('👤 Usuario actual:', currentUser);
    console.log('🔐 Token presente:', !!token);
    console.log('🎭 Rol del usuario:', currentUser?.rol?.nombre);
    
    // Verificar autenticación básica
    if (!token || !currentUser) {
      console.log('❌ Sin token o usuario, redirigiendo a login');
      this.router.navigate(['/login']);
      return false;
    }
    
    // Verificar rol de administrador
    if (currentUser && currentUser.rol) {
      const rolUsuario = currentUser.rol.nombre.toUpperCase();
      console.log('🔍 Rol normalizado:', rolUsuario);
      
      if (rolUsuario === 'ADMINISTRADOR') {
        console.log('✅ AdminGuard - Acceso permitido (ADMINISTRADOR)');
        return true;
      }
    }
    
    console.log('❌ AdminGuard - Acceso denegado, redirigiendo a dashboard');
    this.router.navigate(['/dashboard']);
    return false;
  }
}