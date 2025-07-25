// src/app/admin/usuarios/usuario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Usuario, UsuarioRequest } from '../../shared/models/usuario.model';
import { Rol } from '../../shared/models/rol.model';
import { ApiResponse } from '../../shared/models/api-response.model';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Usuarios
  obtenerUsuarios(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/admin/usuarios`);
  }

  obtenerUsuarioPorId(id: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/admin/usuarios/${id}`);
  }

  crearUsuario(usuario: UsuarioRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/admin/usuarios`, usuario);
  }

  actualizarUsuario(id: number, usuario: UsuarioRequest): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.apiUrl}/admin/usuarios/${id}`, usuario);
  }

  eliminarUsuario(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.apiUrl}/admin/usuarios/${id}`);
  }

  cambiarEstadoUsuario(id: number, estado: boolean): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.apiUrl}/admin/usuarios/${id}/estado?estado=${estado}`, {});
  }

  // Roles
  obtenerRoles(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/admin/roles`);
  }
}