import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';

export interface InsumoRequestDTO {
  nombre: string;
  descripcion?: string;
  codigo: string;
  precio?: number;
  stock: number;
  stockMinimo?: number;
  unidadMedida?: string;
  categoria?: string;
  proveedor?: string;
  estado?: boolean;
}

export interface InsumoUpdateStockDTO {
  cantidad: number;
  tipoOperacion: 'ENTRADA' | 'SALIDA';
  observacion?: string;
}

export interface InsumoFiltroDTO {
  nombre?: string;
  categoria?: string;
  proveedor?: string;
  estado?: boolean;
  soloStockBajo?: boolean;
  stockMinimo?: number;
  stockMaximo?: number;
}

export interface UsuarioSimpleDTO {
  id: number;
  nombre: string;
  apellido: string;
  mail: string;
}

export interface InsumoResponseDTO {
  id: number;
  nombre: string;
  descripcion?: string;
  codigo: string;
  precio?: number;
  stock: number;
  stockMinimo?: number;
  unidadMedida?: string;
  categoria?: string;
  proveedor?: string;
  fechaCreacion: string;
  fechaActualizacion: string;
  estado: boolean;
  stockBajo: boolean;
  usuarioCreador: UsuarioSimpleDTO;
}

export interface ApiResponseDTO {
  exito: boolean;
  mensaje: string;
  datos?: any;
}

@Injectable({
  providedIn: 'root'
})
export class InsumoService {
  private readonly API_URL = 'http://localhost:8080/api/insumos';

  constructor(private http: HttpClient) {}

  // Crear insumo
  crearInsumo(insumo: InsumoRequestDTO): Observable<ApiResponseDTO> {
    console.log('🔧 Creando insumo:', insumo);
    return this.http.post<ApiResponseDTO>(this.API_URL, insumo)
      .pipe(
        tap(response => console.log('✅ Insumo creado:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener todos los insumos
  obtenerTodosLosInsumos(): Observable<ApiResponseDTO> {
    console.log('📋 Obteniendo todos los insumos');
    return this.http.get<ApiResponseDTO>(this.API_URL)
      .pipe(
        tap(response => console.log('📦 Insumos obtenidos:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener insumo por ID
  obtenerInsumoPorId(id: number): Observable<ApiResponseDTO> {
    console.log('🔍 Obteniendo insumo por ID:', id);
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/${id}`)
      .pipe(
        tap(response => console.log('📦 Insumo obtenido:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener insumo por código
  obtenerInsumoPorCodigo(codigo: string): Observable<ApiResponseDTO> {
    console.log('🔍 Obteniendo insumo por código:', codigo);
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/codigo/${codigo}`)
      .pipe(
        tap(response => console.log('📦 Insumo obtenido por código:', response)),
        catchError(this.handleError)
      );
  }

  // Actualizar insumo
  actualizarInsumo(id: number, insumo: InsumoRequestDTO): Observable<ApiResponseDTO> {
    console.log('🔄 Actualizando insumo:', id, insumo);
    return this.http.put<ApiResponseDTO>(`${this.API_URL}/${id}`, insumo)
      .pipe(
        tap(response => console.log('✅ Insumo actualizado:', response)),
        catchError(this.handleError)
      );
  }

  // Eliminar insumo (solo administradores)
  eliminarInsumo(id: number): Observable<ApiResponseDTO> {
    console.log('🗑️ Eliminando insumo:', id);
    return this.http.delete<ApiResponseDTO>(`${this.API_URL}/${id}`)
      .pipe(
        tap(response => console.log('✅ Insumo eliminado:', response)),
        catchError(this.handleError)
      );
  }

  // Actualizar stock
  actualizarStock(id: number, updateStockDTO: InsumoUpdateStockDTO): Observable<ApiResponseDTO> {
    console.log('📊 Actualizando stock:', id, updateStockDTO);
    return this.http.patch<ApiResponseDTO>(`${this.API_URL}/${id}/stock`, updateStockDTO)
      .pipe(
        tap(response => console.log('✅ Stock actualizado:', response)),
        catchError(this.handleError)
      );
  }

  // Cambiar estado (solo administradores)
  cambiarEstadoInsumo(id: number, estado: boolean): Observable<ApiResponseDTO> {
    console.log('🔄 Cambiando estado insumo:', id, estado);
    return this.http.patch<ApiResponseDTO>(`${this.API_URL}/${id}/estado?estado=${estado}`, {})
      .pipe(
        tap(response => console.log('✅ Estado cambiado:', response)),
        catchError(this.handleError)
      );
  }

  // Buscar insumos con filtros
  buscarInsumos(filtro: InsumoFiltroDTO): Observable<ApiResponseDTO> {
    console.log('🔍 Buscando insumos con filtros:', filtro);
    return this.http.post<ApiResponseDTO>(`${this.API_URL}/buscar`, filtro)
      .pipe(
        tap(response => console.log('📋 Resultados de búsqueda:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener insumos con stock bajo
  obtenerInsumosConStockBajo(): Observable<ApiResponseDTO> {
    console.log('⚠️ Obteniendo insumos con stock bajo');
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/stock-bajo`)
      .pipe(
        tap(response => console.log('📋 Insumos con stock bajo:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener categorías
  obtenerCategorias(): Observable<ApiResponseDTO> {
    console.log('📂 Obteniendo categorías');
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/categorias`)
      .pipe(
        tap(response => console.log('📂 Categorías obtenidas:', response)),
        catchError(this.handleError)
      );
  }

  // Obtener proveedores
  obtenerProveedores(): Observable<ApiResponseDTO> {
    console.log('🏢 Obteniendo proveedores');
    return this.http.get<ApiResponseDTO>(`${this.API_URL}/proveedores`)
      .pipe(
        tap(response => console.log('🏢 Proveedores obtenidos:', response)),
        catchError(this.handleError)
      );
  }

  private handleError = (error: HttpErrorResponse): Observable<never> => {
    console.error('💥 Error en InsumoService:', error);
    
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
          } else if (error.error && error.error.datos) {
            // Errores de validación
            const validationErrors = Object.entries(error.error.datos)
              .map(([field, message]) => `${field}: ${message}`)
              .join(', ');
            errorMessage = `Errores de validación: ${validationErrors}`;
          } else {
            errorMessage = 'Datos inválidos';
          }
          break;
        case 401:
          errorMessage = 'No autorizado';
          break;
        case 403:
          errorMessage = 'Acceso denegado - Permisos insuficientes';
          break;
        case 404:
          errorMessage = 'Insumo no encontrado';
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