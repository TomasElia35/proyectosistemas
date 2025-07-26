import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('%c🔄 INTERCEPTOR EJECUTÁNDOSE', 'color: blue; font-weight: bold');
    console.log('🌐 URL de la petición:', req.url);
    console.log('📝 Método HTTP:', req.method);
    
    // Obtener el token del servicio de autenticación
    const token = this.authService.getToken();
    console.log('🔐 Token obtenido del servicio:', token ? 'PRESENTE' : 'AUSENTE');
    
    if (token) {
      console.log('🔐 Token (primeros 30 chars):', token.substring(0, 30) + '...');
      
      // Clonar la request y agregar el header Authorization
      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      
      console.log('✅ Header Authorization agregado');
      console.log('📋 Headers de la petición clonada:');
      authReq.headers.keys().forEach(key => {
        const value = authReq.headers.get(key);
        if (key === 'Authorization') {
          console.log(`   ${key}: Bearer ${value?.substring(7, 37)}...`);
        } else {
          console.log(`   ${key}: ${value}`);
        }
      });
      
      return next.handle(authReq).pipe(
        tap(
          (event) => {
            console.log('✅ Petición exitosa con token');
          },
          (error) => {
            console.error('❌ Error en petición con token:', error.status, error.statusText);
          }
        )
      );
    }

    console.log('❌ No hay token, enviando petición sin Authorization header');
    
    return next.handle(req).pipe(
      tap(
        (event) => {
          console.log('✅ Petición exitosa sin token');
        },
        (error) => {
          console.error('❌ Error en petición sin token:', error.status, error.statusText);
        }
      )
    );
  }
}