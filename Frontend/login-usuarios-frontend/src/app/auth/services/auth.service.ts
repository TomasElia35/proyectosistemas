import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface LoginRequest {
  mail: string;
  contrasena: string;
}

export interface RolDTO {
  id: number;
  nombre: string;
}

export interface UsuarioResponsiveDTO {
  id: number;
  nombre: string;
  apellido: string;
  mail: string;
  estado: boolean;
  fechaCreacion: string;
  rol: RolDTO;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  usuario: UsuarioResponsiveDTO;
  mensaje: string;
}

export interface ApiResponse {
  exito: boolean;
  mensaje: string;
  datos?: any;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly API_URL = 'http://localhost:8080/api/auth';
  private currentUserSubject = new BehaviorSubject<UsuarioResponsiveDTO | null>(null);
  private tokenSubject = new BehaviorSubject<string | null>(null);

  public currentUser$ = this.currentUserSubject.asObservable();
  public token$ = this.tokenSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadStoredData();
  }

  private loadStoredData(): void {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('currentUser');

    if (token) {
      this.tokenSubject.next(token);
    }

    if (user) {
      try {
        const userData = JSON.parse(user);
        this.currentUserSubject.next(userData);
      } catch (error) {
        console.error('Error parsing stored user data:', error);
        this.logout();
      }
    }
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });

    // 🔍 DEBUG MÁS DETALLADO
    console.log('%c🔍 ENVIANDO PETICIÓN DE LOGIN', 'color: blue; font-weight: bold');
    console.log('📍 URL:', `${this.API_URL}/login`);
    console.log('📧 Email enviado:', `"${credentials.mail}"`);
    console.log('🔐 Contraseña enviada:', `"${credentials.contrasena}" (longitud: ${credentials.contrasena.length})`);
    console.log('📋 Headers:', headers);
    console.log('📦 Body completo:', JSON.stringify(credentials, null, 2));
    
    // Verificar formato del email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    console.log('✉️ ¿Email válido?', emailRegex.test(credentials.mail));
    
    // Verificar longitud de contraseña
    console.log('🔑 ¿Contraseña > 6 chars?', credentials.contrasena.length >= 6);

    return this.http.post<LoginResponse>(`${this.API_URL}/login`, credentials, { headers })
      .pipe(
        tap(response => {
          console.log('%c✅ RESPUESTA EXITOSA', 'color: green; font-weight: bold');
          console.log('🎉 Respuesta completa:', response);
          
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response.usuario));
          
          this.tokenSubject.next(response.token);
          this.currentUserSubject.next(response.usuario);
        }),
        catchError(this.handleError)
      );
  }

  logout(): void {
    console.log('🚪 Cerrando sesión...');
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.tokenSubject.next(null);
    this.currentUserSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;
  }

  getCurrentUser(): UsuarioResponsiveDTO | null {
    return this.currentUserSubject.value;
  }

  getToken(): string | null {
    return this.tokenSubject.value;
  }

  private handleError = (error: HttpErrorResponse): Observable<never> => {
    console.log('%c❌ ERROR EN PETICIÓN HTTP', 'color: red; font-weight: bold');
    console.log('🔴 Error completo:', error);
    
    // 🔍 INFORMACIÓN DETALLADA DEL ERROR
    console.log('📊 Status Code:', error.status);
    console.log('📝 Status Text:', error.statusText);
    console.log('🌐 URL:', error.url);
    console.log('📦 Response Body:', error.error);
    
    // Intentar mostrar la respuesta del backend
    if (error.error) {
      console.log('%c📋 RESPUESTA DEL BACKEND:', 'color: orange; font-weight: bold');
      if (typeof error.error === 'object') {
        console.log('📄 Tipo: Objeto JSON');
        console.log('🔍 Contenido:', JSON.stringify(error.error, null, 2));
        
        // Verificar si es una respuesta de validación de Spring Boot
        if (error.error.mensaje) {
          console.log('💬 Mensaje del backend:', error.error.mensaje);
        }
        if (error.error.datos) {
          console.log('📋 Datos adicionales:', error.error.datos);
        }
        if (error.error.timestamp) {
          console.log('⏰ Timestamp:', error.error.timestamp);
        }
        if (error.error.path) {
          console.log('🛤️ Path:', error.error.path);
        }
      } else {
        console.log('📄 Tipo: Texto plano');
        console.log('📝 Contenido:', error.error);
      }
    }
    
    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error de conexión: ${error.error.message}`;
      console.log('🔌 Tipo de error: Cliente/Conexión');
    } else {
      console.log('🖥️ Tipo de error: Servidor');
      
      switch (error.status) {
        case 0:
          errorMessage = 'No se puede conectar con el servidor. Verifica que el backend esté ejecutándose en localhost:8080';
          break;
        case 400:
          console.log('%c🔍 ANALIZANDO ERROR 400', 'color: orange; font-weight: bold');
          
          if (error.error && typeof error.error === 'object') {
            // Caso 1: Respuesta estructurada del backend
            if (error.error.mensaje) {
              errorMessage = error.error.mensaje;
              console.log('📝 Mensaje del backend usado:', error.error.mensaje);
            } 
            // Caso 2: Errores de validación de Bean Validation
            else if (error.error.datos && typeof error.error.datos === 'object') {
              const validationErrors = Object.entries(error.error.datos)
                .map(([field, message]) => `${field}: ${message}`)
                .join(', ');
              errorMessage = `Errores de validación: ${validationErrors}`;
              console.log('📋 Errores de validación encontrados:', error.error.datos);
            }
            // Caso 3: Formato de error estándar de Spring Boot
            else if (error.error.error) {
              errorMessage = error.error.error;
              console.log('⚠️ Error estándar de Spring Boot:', error.error.error);
            }
            else {
              errorMessage = 'Datos inválidos - formato no reconocido';
              console.log('❓ Formato de error no reconocido');
            }
          } 
          // Caso 4: Respuesta de texto plano
          else if (typeof error.error === 'string') {
            errorMessage = error.error;
            console.log('📄 Respuesta de texto plano:', error.error);
          } 
          else {
            errorMessage = 'Datos inválidos - Verifica email y contraseña';
            console.log('❓ Sin información adicional del error');
          }
          break;
        case 401:
          errorMessage = 'Credenciales inválidas. Verifica tu email y contraseña.';
          break;
        case 403:
          errorMessage = 'Acceso denegado - Tu cuenta puede estar desactivada';
          break;
        case 404:
          errorMessage = 'Servicio no encontrado. Verifica que el backend esté en localhost:8080';
          break;
        case 500:
          errorMessage = 'Error interno del servidor. Revisa los logs del backend.';
          break;
        default:
          errorMessage = `Error ${error.status}: ${error.statusText || 'Sin descripción'}`;
      }
    }

    console.log('%c📢 MENSAJE FINAL PARA EL USUARIO:', 'color: purple; font-weight: bold');
    console.log(errorMessage);
    
    return throwError(() => errorMessage);
  }
}