import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface UsuarioRequestDTO {
  nombre: string;
  apellido: string;
  mail: string;
  contrasena: string;
  estado: boolean;
  rol: number;
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

export interface ApiResponseDTO {
  exito: boolean;
  mensaje: string;
  datos?: any;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly API_URL = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  // Crear usuario
  crearUsuario(usuario: UsuarioRequestDTO): Observable<ApiResponseDTO> {
    return this.http.post<ApiResponseDTO>(`${this.API_URL}/usuarios`, usuario)
      .pipe(catchError(this.handleError));
  }

  // Obtener todos los usuarios
  obtenerTodosLosUsuarios(): Observable<ApiResponseDTO> {
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/usuarios`)
      .pipe(catchError(this.handleError));
  }

  // Obtener usuario por ID
  obtenerUsuarioPorId(id: number): Observable<ApiResponseDTO> {
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/usuarios/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Actualizar usuario
  actualizarUsuario(id: number, usuario: UsuarioRequestDTO): Observable<ApiResponseDTO> {
    return this.http.put<ApiResponseDTO>(`${this.API_URL}/usuarios/${id}`, usuario)
      .pipe(catchError(this.handleError));
  }

  // Eliminar usuario
  eliminarUsuario(id: number): Observable<ApiResponseDTO> {
    return this.http.delete<ApiResponseDTO>(`${this.API_URL}/usuarios/${id}`)
      .pipe(catchError(this.handleError));
  }

  // Cambiar estado del usuario
  cambiarEstadoUsuario(id: number, estado: boolean): Observable<ApiResponseDTO> {
    return this.http.patch<ApiResponseDTO>(`${this.API_URL}/usuarios/${id}/estado?estado=${estado}`, {})
      .pipe(catchError(this.handleError));
  }

  // Obtener todos los roles
  obtenerTodosLosRoles(): Observable<ApiResponseDTO> {
    console.log('🔍 UserService: Obteniendo roles desde:', `${this.API_URL}/roles`);
    
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/roles`)
      .pipe(
        tap(response => {
          console.log('📋 UserService: Respuesta de roles recibida:', response);
        }),
        catchError(this.handleError)
      );
  }

  private handleError = (error: HttpErrorResponse): Observable<never> => {
    console.error('Error en UserService:', error);
    
    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error de conexión: ${error.error.message}`;
    } else {
      switch (error.status) {
        case 0:
          errorMessage = 'No se puede conectar con el servidor';
          break;
        case 400:
          if (error.error && error.error.mensaje) {
            errorMessage = error.error.mensaje;
          } else {
            errorMessage = 'Datos inválidos';
          }
          break;
        case 401:
          errorMessage = 'No autorizado';
          break;
        case 403:
          errorMessage = 'Acceso denegado';
          break;
        case 404:
          errorMessage = 'Recurso no encontrado';
          break;
        case 500:
          errorMessage = 'Error interno del servidor';
          break;
        default:
          errorMessage = `Error ${error.status}: ${error.statusText}`;
      }
    }

    return throwError(() => errorMessage);
  }
}