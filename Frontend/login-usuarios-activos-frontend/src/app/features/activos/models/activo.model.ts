export interface ActivoTecnologicoRequestDTO {
  codigoInventario: string;
  codigoTecnico?: string;
  nombre: string;
  descripcion?: string;
  idModeloArticulo: number;
  idProveedor: number;
  costo: number;
  fechaAdquisicion: string; // formato ISO (yyyy-MM-dd)
  garantiaMeses: number;
  fechaVencimientoGarantia?: string;
  idEstado: number;
  idEmpleado: number;
  observaciones?: string;
  usuarioCreacion: string;
  usuarioModificacion?: string;
  activo: boolean;
}

export interface ActivoTecnologicoResponseDTO {
  id: number;
  codigoInventario: string;
  codigoTecnico?: string;
  nombre: string;
  descripcion?: string;

  idModeloArticulo: number;
  modeloArticuloMarca: string;
  modeloArticuloModelo: string;

  idProveedor: number;
  proveedorNombre: string;

  costo: number;
  fechaAdquisicion: string;
  garantiaMeses: number;
  fechaVencimientoGarantia?: string;

  idEstado: number;
  estadoNombre: string;

  idEmpleado: number;
  empleadoNombreCompleto: string;
  idArea: number;
  areaNombre: string;

  observaciones?: string;
  usuarioCreacion: string;
  usuarioModificacion?: string;
  activo: boolean;

  fechaCreacion: string;
  fechaUltimaModificacion: string;
}
