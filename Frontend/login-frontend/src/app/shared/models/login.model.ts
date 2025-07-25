import { Usuario } from "./usuario.model";

export interface LoginRequest {
  mail: string;
  contrasena: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  usuario: Usuario;
  mensaje: string;
}