// 1. ELIMINA el contenido de auth.interceptor.ts y reemplázalo con esto:
// src/app/auth/interceptors/auth.interceptor.ts

import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('%c🔄 INTERCEPTOR EJECUTÁNDOSE', 'color: blue; font-weight: bold');
  console.log('🌐 URL:', req.url);
  console.log('📝 Método:', req.method);
  
  // Obtener el token directamente del localStorage (más confiable)
  const token = localStorage.getItem('token');
  console.log('🔐 Token obtenido:', token ? 'PRESENTE' : 'AUSENTE');
  
  if (token) {
    console.log('🔐 Token (primeros 30 chars):', token.substring(0, 30) + '...');
    
    // Clonar la request y agregar headers
    const authReq = req.clone({
      setHeaders: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    });
    
    console.log('✅ Headers agregados:');
    console.log('   Authorization: Bearer ' + token.substring(0, 20) + '...');
    console.log('   Content-Type: application/json');
    console.log('   Accept: application/json');
    
    // Manejar errores de autenticación
    return next(authReq).pipe(
      catchError(error => {
        if (error.status === 401) {
          console.log('❌ Token expirado o inválido, limpiando localStorage');
          localStorage.removeItem('token');
          localStorage.removeItem('currentUser');
          
          const router = inject(Router);
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  console.log('❌ Sin token - enviando request original');
  return next(req);
};