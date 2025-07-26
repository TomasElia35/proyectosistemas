import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
    // Verificar si hay datos guardados al inicializar el servicio
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
    return this.http.post<LoginResponse>(`${this.API_URL}/login`, credentials)
      .pipe(
        tap(response => {
          // Guardar token y usuario en localStorage
          localStorage.setItem('token', response.token);
          localStorage.setItem('currentUser', JSON.stringify(response.usuario));
          
          // Actualizar subjects
          this.tokenSubject.next(response.token);
          this.currentUserSubject.next(response.usuario);
        }),
        catchError(this.handleError)
      );
  }

  logout(): void {
    // Limpiar localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    
    // Limpiar subjects
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

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del lado del servidor
      switch (error.status) {
        case 400:
          if (error.error && error.error.mensaje) {
            errorMessage = error.error.mensaje;
          } else {
            errorMessage = 'Datos inválidos';
          }
          break;
        case 401:
          errorMessage = 'Credenciales inválidas';
          break;
        case 403:
          errorMessage = 'Acceso denegado';
          break;
        case 500:
          errorMessage = 'Error interno del servidor';
          break;
        default:
          errorMessage = `Error ${error.status}: ${error.message}`;
      }
    }

    return throwError(() => errorMessage);
  }
}