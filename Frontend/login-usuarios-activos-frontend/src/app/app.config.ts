// src/app/app.config.ts - SOLUCIÓN DEFINITIVA
import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { HttpRequest, HttpHandlerFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { routes } from './app.routes';
import { AuthService } from './auth/services/auth.service';

// Interceptor funcional que SÍ funciona
export function authInterceptor(req: HttpRequest<unknown>, next: HttpHandlerFn) {
  console.log('%c🔄 INTERCEPTOR EJECUTÁNDOSE', 'color: blue; font-weight: bold');
  console.log('🌐 URL:', req.url);
  console.log('📝 Método:', req.method);
  
  // Inyectar el AuthService
  const authService = inject(AuthService);
  const token = authService.getToken();

  console.log('🔐 Token obtenido:', token ? 'PRESENTE' : 'AUSENTE');
  
  if (token) {
    console.log('🔐 Token (primeros 30 chars):', token.substring(0, 30) + '...');
    
    // Clonar la request y agregar el header
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
    
    return next(authReq);
  }

  console.log('❌ Sin token - enviando request original');
  return next(req);
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideHttpClient(withInterceptors([authInterceptor]))
    // ❌ IMPORTANTE: NO agregues HTTP_INTERCEPTORS aquí
  ]
};
