// import { Injectable } from '@angular/core';
// import { CanActivate, Router } from '@angular/router';
// import { AuthService } from '../services/auth.service';

// @Injectable({
//   providedIn: 'root'
// })
// export class AuthGuard implements CanActivate {

//   constructor(
//     private authService: AuthService,
//     private router: Router
//   ) {}

//   canActivate(): boolean {
//     console.log('%c🔍 AUTH GUARD EJECUTÁNDOSE', 'color: green; font-weight: bold');
    
//     const isAuthenticated = this.authService.isAuthenticated();
//     const currentUser = this.authService.getCurrentUser();
//     const token = this.authService.getToken();
    
//     console.log('🔐 ¿Está autenticado?', isAuthenticated);
//     console.log('👤 Usuario actual:', currentUser?.nombre);
//     console.log('🎭 Rol:', currentUser?.rol?.nombre);
//     console.log('🔑 Token presente:', !!token);
    
//     if (isAuthenticated) {
//       console.log('✅ AuthGuard - Acceso permitido');
//       return true;
//     } else {
//       console.log('❌ AuthGuard - Acceso denegado, redirigiendo a login');
//       this.router.navigate(['/login']);
//       return false;
//     }
//   }
// }
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): boolean {
    console.log('%c🔍 AUTH GUARD EJECUTÁNDOSE', 'color: green; font-weight: bold');
    
    const isAuthenticated = this.authService.isAuthenticated();
    const currentUser = this.authService.getCurrentUser();
    const token = this.authService.getToken();
    
    console.log('🔐 ¿Está autenticado?', isAuthenticated);
    console.log('👤 Usuario actual:', currentUser?.nombre);
    console.log('🎭 Rol:', currentUser?.rol?.nombre);
    console.log('🔑 Token presente:', !!token);
    
    if (isAuthenticated) {
      console.log('✅ AuthGuard - Acceso permitido');
      return true;
    } else {
      console.log('❌ AuthGuard - Acceso denegado, redirigiendo a login');
      this.router.navigate(['/login']);
      return false;
    }
  }
}