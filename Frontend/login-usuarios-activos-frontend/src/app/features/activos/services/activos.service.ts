import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivoTecnologicoRequestDTO, ActivoTecnologicoResponseDTO } from '../models/activo.model';

@Injectable({
  providedIn: 'root'
})
export class ActivosService {

  private baseUrl = '/api/activos';

  constructor(private http: HttpClient) {}

  crear(dto: ActivoTecnologicoRequestDTO): Observable<ActivoTecnologicoResponseDTO> {
    return this.http.post<ActivoTecnologicoResponseDTO>(this.baseUrl, dto);
  }

  obtenerPorId(id: number): Observable<ActivoTecnologicoResponseDTO> {
    return this.http.get<ActivoTecnologicoResponseDTO>(`${this.baseUrl}/${id}`);
  }

  actualizar(id: number, dto: ActivoTecnologicoRequestDTO): Observable<ActivoTecnologicoResponseDTO> {
    return this.http.put<ActivoTecnologicoResponseDTO>(`${this.baseUrl}/${id}`, dto);
  }

  listar(): Observable<ActivoTecnologicoResponseDTO[]> {
    return this.http.get<ActivoTecnologicoResponseDTO[]>(this.baseUrl);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  // 🔻 Métodos auxiliares

  getModelos(): Observable<any[]> {
    return this.http.get<any[]>('/api/modelos');
  }

  getProveedores(): Observable<any[]> {
    return this.http.get<any[]>('/api/proveedores');
  }

  getEstados(): Observable<any[]> {
    return this.http.get<any[]>('/api/estados');
  }

  getEmpleados(): Observable<any[]> {
    return this.http.get<any[]>('/api/empleados');
  }
}


