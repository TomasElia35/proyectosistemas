// src/app/auth/interceptors/auth.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  console.log('%c🔄 INTERCEPTOR EJECUTÁNDOSE', 'color: blue; font-weight: bold');
  console.log('🌐 URL de la petición:', req.url);
  console.log('📝 Método HTTP:', req.method);
  
  // Obtener el token del localStorage
  const token = localStorage.getItem('token');
  console.log('🔐 Token obtenido:', token ? 'PRESENTE' : 'AUSENTE');
  
  if (token) {
    console.log('🔐 Token (primeros 30 chars):', token.substring(0, 30) + '...');
    
    // Clonar la request y agregar el header Authorization
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    
    console.log('✅ Header Authorization agregado');
    console.log('📋 Headers agregados:', authReq.headers.keys());
    
    return next(authReq);
  }

  console.log('❌ No hay token, enviando petición sin Authorization header');
  return next(req);
};