import { Rol } from "./rol.model";

export interface Usuario {
  id?: number;
  nombre: string;
  apellido: string;
  mail: string;
  contrasena?: string;
  estado: boolean;
  fechaCreacion?: string;
  rol: Rol;
}

export interface UsuarioRequest {
  nombre: string;
  apellido: string;
  mail: string;
  contrasena: string;
  estado: boolean;
  rol: number;
}