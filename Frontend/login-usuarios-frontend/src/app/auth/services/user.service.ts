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
// Obtener todos los roles - VERSION CON DEBUG COMPLETO
obtenerTodosLosRoles(): Observable<ApiResponseDTO> {
  console.log('%c🔍 INICIANDO PETICIÓN DE ROLES', 'color: blue; font-weight: bold');
  console.log('🌐 URL completa:', `${this.API_URL}/roles`);
  console.log('🔧 API_URL base:', this.API_URL);
  
  // Verificar token
  const token = localStorage.getItem('token');
  console.log('🔐 Token disponible:', token ? 'SÍ' : 'NO');
  
  // Verificar usuario
  const currentUser = localStorage.getItem('currentUser');
  if (currentUser) {
    try {
      const user = JSON.parse(currentUser);
      console.log('👤 Usuario actual:', user.nombre, user.apellido);
      console.log('🎭 Rol del usuario:', user.rol?.nombre);
    } catch (e) {
      console.error('❌ Error al parsear usuario actual:', e);
    }
  }
  
  return this.http.get<ApiResponseDTO>(`${this.API_URL}/roles`)
    .pipe(
      tap(response => {
        console.log('%c📋 RESPUESTA RECIBIDA', 'color: green; font-weight: bold');
        console.log('📦 Respuesta completa:', response);
        console.log('📊 Tipo de respuesta:', typeof response);
        
        // Verificar estructura de la respuesta
        if (response) {
          console.log('🔍 Propiedades de la respuesta:');
          Object.keys(response).forEach(key => {
            console.log(`   ${key}:`, response[key as keyof ApiResponseDTO]);
          });
          
          if (response.exito !== undefined) {
            console.log('✅ Estructura ApiResponseDTO detectada');
            console.log('   exito:', response.exito);
            console.log('   mensaje:', response.mensaje);
            console.log('   datos:', response.datos);
            
            if (response.exito) {
              if (response.datos && Array.isArray(response.datos)) {
                console.log('📊 Cantidad de roles en datos:', response.datos.length);
                response.datos.forEach((rol: any, index: number) => {
                  console.log(`   Rol ${index + 1}:`, {
                    id: rol.id,
                    nombre: rol.nombre,
                    tipo: typeof rol
                  });
                });
                
                if (response.datos.length === 0) {
                  console.warn('⚠️ Array de roles está vacío');
                }
              } else {
                console.error('❌ response.datos no es un array:', typeof response.datos, response.datos);
              }
            } else {
              console.error('❌ response.exito es false:', response.mensaje);
            }
          } else {
            console.error('❌ Respuesta no tiene estructura ApiResponseDTO');
          }
        } else {
          console.error('❌ Respuesta es null o undefined');
        }
      }),
      catchError((error) => {
        console.log('%c💥 ERROR EN PETICIÓN', 'color: red; font-weight: bold');
        console.error('🚨 Error completo:', error);
        console.error('📊 Status:', error.status);
        console.error('🌐 URL que falló:', error.url);
        console.error('📝 Mensaje:', error.message);
        console.error('📦 Error body:', error.error);
        
        // Análisis específico del error
        if (error.status === 0) {
          console.error('💀 Error de conectividad - Backend no disponible');
        } else if (error.status === 401) {
          console.error('🔐 Error de autenticación - Token inválido');
        } else if (error.status === 403) {
          console.error('🚫 Error de autorización - Sin permisos');
        } else if (error.status === 404) {
          console.error('🔍 Endpoint no encontrado');
        } else if (error.status >= 500) {
          console.error('🖥️ Error del servidor');
        }
        
        return this.handleError(error);
      })
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