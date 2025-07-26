import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { UserService, UsuarioRequestDTO, RolDTO } from '../../services/user.service';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="user-form-container">
      <div class="header">
        <div class="header-content">
          <h1>{{ isEditMode ? 'Editar Usuario' : 'Crear Usuario' }}</h1>
          <button class="btn btn-secondary" (click)="volver()">
            <i class="icon">←</i>
            Volver
          </button>
        </div>
      </div>

      <!-- Mensajes -->
      <div class="messages" *ngIf="successMessage || errorMessage">
        <div *ngIf="successMessage" class="alert alert-success">
          <i class="icon-success">✓</i>
          {{ successMessage }}
        </div>
        <div *ngIf="errorMessage" class="alert alert-error">
          <i class="icon-error">⚠</i>
          {{ errorMessage }}
        </div>
      </div>

      <!-- Loading -->
      <div *ngIf="isLoading" class="loading-container">
        <div class="spinner"></div>
        <p>{{ isEditMode ? 'Cargando datos del usuario...' : 'Cargando formulario...' }}</p>
      </div>

      <!-- Formulario -->
      <div *ngIf="!isLoading" class="form-container">
        <div class="form-wrapper">
          <form [formGroup]="userForm" (ngSubmit)="onSubmit()" class="user-form">
            
            <!-- Nombre -->
            <div class="form-group">
              <label for="nombre" class="form-label">Nombre *</label>
              <input
                type="text"
                id="nombre"
                formControlName="nombre"
                class="form-input"
                [class.error]="nombre?.invalid && nombre?.touched"
                placeholder="Ingresa el nombre"
                (focus)="clearMessages()"
              >
              <div *ngIf="nombre?.invalid && nombre?.touched" class="error-message">
                {{ getFieldErrorMessage('nombre') }}
              </div>
            </div>

            <!-- Apellido -->
            <div class="form-group">
              <label for="apellido" class="form-label">Apellido *</label>
              <input
                type="text"
                id="apellido"
                formControlName="apellido"
                class="form-input"
                [class.error]="apellido?.invalid && apellido?.touched"
                placeholder="Ingresa el apellido"
                (focus)="clearMessages()"
              >
              <div *ngIf="apellido?.invalid && apellido?.touched" class="error-message">
                {{ getFieldErrorMessage('apellido') }}
              </div>
            </div>

            <!-- Email -->
            <div class="form-group">
              <label for="mail" class="form-label">Correo Electrónico *</label>
              <input
                type="email"
                id="mail"
                formControlName="mail"
                class="form-input"
                [class.error]="mail?.invalid && mail?.touched"
                placeholder="ejemplo@correo.com"
                (focus)="clearMessages()"
              >
              <div *ngIf="mail?.invalid && mail?.touched" class="error-message">
                {{ getFieldErrorMessage('mail') }}
              </div>
            </div>

            <!-- Contraseña -->
            <div class="form-group">
              <label for="contrasena" class="form-label">
                Contraseña {{ isEditMode ? '(dejar vacío para mantener actual)' : '*' }}
              </label>
              <input
                type="password"
                id="contrasena"
                formControlName="contrasena"
                class="form-input"
                [class.error]="contrasena?.invalid && contrasena?.touched"
                [placeholder]="isEditMode ? 'Nueva contraseña (opcional)' : 'Ingresa la contraseña'"
                (focus)="clearMessages()"
              >
              <div *ngIf="contrasena?.invalid && contrasena?.touched" class="error-message">
                {{ getFieldErrorMessage('contrasena') }}
              </div>
            </div>

            <!-- Rol -->
            <!-- En user-form.component.html -->
<div class="form-group">
  <label for="rol" class="form-label">Rol *</label>
  
  <!-- Debug info -->
  <div *ngIf="roles.length === 0" class="debug-info">
    <small style="color: orange;">⚠️ No hay roles disponibles. Verificando...</small>
  </div>
  
  <select
    id="rol"
    formControlName="rol"
    class="form-select"
    [class.error]="rol?.invalid && rol?.touched"
    (focus)="clearMessages(); cargarRoles()"
  >
    <option value="">
      {{ roles.length === 0 ? 'Cargando roles...' : 'Selecciona un rol' }}
    </option>
    <option *ngFor="let rolOption of roles; trackBy: trackByRolId" 
            [value]="rolOption.id">
      {{ rolOption.nombre }}
    </option>
  </select>
  
  <!-- Debug: mostrar roles cargados -->
  <div *ngIf="roles.length > 0" class="debug-info">
    <small style="color: green;">✅ {{ roles.length }} roles cargados</small>
  </div>
</div>

            <!-- Estado -->
            <div class="form-group">
              <div class="checkbox-wrapper">
                <label class="checkbox-label">
                  <input
                    type="checkbox"
                    formControlName="estado"
                    class="checkbox-input"
                  >
                  <span class="checkbox-custom"></span>
                  Usuario activo
                </label>
              </div>
            </div>

            <!-- Botones -->
            <div class="form-actions">
              <button
                type="button"
                class="btn btn-secondary"
                (click)="volver()"
                [disabled]="isSubmitting"
              >
                Cancelar
              </button>
              
              <button
                type="submit"
                class="btn btn-primary"
                [disabled]="isSubmitting || userForm.invalid"
                [class.loading]="isSubmitting"
              >
                <span *ngIf="!isSubmitting">
                  {{ isEditMode ? 'Actualizar Usuario' : 'Crear Usuario' }}
                </span>
                <span *ngIf="isSubmitting" class="loading-content">
                  <div class="spinner"></div>
                  {{ isEditMode ? 'Actualizando...' : 'Creando...' }}
                </span>
              </button>
            </div>

          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .user-form-container {
      min-height: 100vh;
      background-color: #f8f9fa;
    }

    .header {
      background: white;
      border-bottom: 1px solid #dee2e6;
      padding: 1.5rem 2rem;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      max-width: 800px;
      margin: 0 auto;
    }

    .header h1 {
      margin: 0;
      color: #333;
      font-size: 1.8rem;
    }

    .btn {
      padding: 0.75rem 1.5rem;
      border: none;
      border-radius: 6px;
      font-weight: 500;
      cursor: pointer;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      transition: all 0.3s ease;
      text-decoration: none;
    }

    .btn-primary {
      background: #007bff;
      color: white;
      min-height: 48px;
      justify-content: center;
    }

    .btn-primary:hover:not(:disabled) {
      background: #0056b3;
      transform: translateY(-1px);
    }

    .btn-primary:disabled {
      background: #6c757d;
      cursor: not-allowed;
      transform: none;
    }

    .btn-secondary {
      background: #6c757d;
      color: white;
    }

    .btn-secondary:hover:not(:disabled) {
      background: #545b62;
    }

    .messages {
      max-width: 800px;
      margin: 1rem auto;
      padding: 0 2rem;
    }

    .alert {
      padding: 1rem;
      border-radius: 6px;
      display: flex;
      align-items: center;
      gap: 0.5rem;
      margin-bottom: 1rem;
    }

    .alert-success {
      background: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
    }

    .alert-error {
      background: #f8d7da;
      color: #721c24;
      border: 1px solid #f5c6cb;
    }

    .loading-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 4rem;
      gap: 1rem;
    }

    .spinner {
      width: 20px;
      height: 20px;
      border: 2px solid #f3f3f3;
      border-top: 2px solid #007bff;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }

    .form-container {
      max-width: 800px;
      margin: 0 auto;
      padding: 2rem;
    }

    .form-wrapper {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
      padding: 2rem;
    }

    .user-form {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
    }

    .form-group {
      display: flex;
      flex-direction: column;
    }

    .form-label {
      color: #333;
      font-size: 14px;
      font-weight: 600;
      margin-bottom: 8px;
    }

    .form-input,
    .form-select {
      padding: 12px 16px;
      border: 2px solid #e1e5e9;
      border-radius: 8px;
      font-size: 16px;
      transition: all 0.3s ease;
      background-color: #fff;
    }

    .form-input:focus,
    .form-select:focus {
      outline: none;
      border-color: #007bff;
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    .form-input.error,
    .form-select.error {
      border-color: #dc3545;
      box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
    }

    .form-input::placeholder {
      color: #adb5bd;
    }

    .error-message {
      color: #dc3545;
      font-size: 12px;
      margin-top: 6px;
      font-weight: 500;
    }

    .checkbox-wrapper {
      display: flex;
      align-items: center;
      margin-top: 0.5rem;
    }

    .checkbox-label {
      display: flex;
      align-items: center;
      cursor: pointer;
      user-select: none;
      gap: 0.75rem;
      font-weight: 500;
      color: #333;
    }

    .checkbox-input {
      position: absolute;
      opacity: 0;
      cursor: pointer;
    }

    .checkbox-custom {
      width: 20px;
      height: 20px;
      border: 2px solid #e1e5e9;
      border-radius: 4px;
      display: flex;
      align-items: center;
      justify-content: center;
      transition: all 0.3s ease;
      background: white;
    }

    .checkbox-input:checked + .checkbox-custom {
      background: #007bff;
      border-color: #007bff;
    }

    .checkbox-input:checked + .checkbox-custom::after {
      content: '✓';
      color: white;
      font-size: 12px;
      font-weight: bold;
    }

    .checkbox-input:focus + .checkbox-custom {
      box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
    }

    .form-actions {
      display: flex;
      gap: 1rem;
      justify-content: flex-end;
      margin-top: 2rem;
      padding-top: 1.5rem;
      border-top: 1px solid #dee2e6;
    }

    .loading-content {
      display: flex;
      align-items: center;
      gap: 0.5rem;
    }

    /* Responsive */
    @media (max-width: 768px) {
      .header-content {
        flex-direction: column;
        gap: 1rem;
        align-items: stretch;
      }

      .form-container {
        padding: 1rem;
      }

      .form-wrapper {
        padding: 1.5rem;
      }

      .form-actions {
        flex-direction: column-reverse;
      }

      .btn {
        width: 100%;
        justify-content: center;
      }
    }
  `]
})

export class UserFormComponent implements OnInit {
  userForm: FormGroup;
  roles: RolDTO[] = [];
  isEditMode = false;
  userId: number | null = null;
  isLoading = false;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.userForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      mail: ['', [Validators.required, Validators.email]],
      contrasena: [''],
      rol: ['', [Validators.required]],
      estado: [true]
    });
  }

  ngOnInit(): void {
    this.checkEditMode();
    this.cargarRoles();
    this.testTokenManually();
  }
  // En user-form.component.ts
trackByRolId(index: number, rol: RolDTO): number {
  return rol.id;
}

  checkEditMode(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.userId = parseInt(id, 10);
      
      // En modo edición, la contraseña es opcional
      this.userForm.get('contrasena')?.setValidators([Validators.minLength(6)]);
      
      this.cargarDatosUsuario();
    } else {
      // En modo creación, la contraseña es obligatoria
      this.userForm.get('contrasena')?.setValidators([Validators.required, Validators.minLength(6)]);
    }
    
    this.userForm.get('contrasena')?.updateValueAndValidity();
  }

// En user-form.component.ts - Método mejorado
cargarRoles(): void {
  console.log('🔍 INICIANDO CARGA DE ROLES');
  
  // Verificar autenticación
  const token = localStorage.getItem('token');
  const currentUser = JSON.parse(localStorage.getItem('currentUser') || '{}');
  
  console.log('🔐 Token presente:', !!token);
  console.log('👤 Usuario:', currentUser.nombre);
  console.log('🎭 Rol usuario:', currentUser.rol?.nombre);
  
  this.userService.obtenerTodosLosRoles().subscribe({
    next: (response) => {
      console.log('✅ Respuesta roles:', response);
      
      if (response && response.exito && Array.isArray(response.datos)) {
        this.roles = response.datos;
        console.log('📋 Roles cargados:', this.roles.length);
        console.log('📋 Lista:', this.roles);
      } else {
        console.error('❌ Formato de respuesta incorrecto:', response);
        this.errorMessage = 'Error en el formato de respuesta de roles';
      }
    },
    error: (error) => {
      console.error('💥 Error cargando roles:', error);
      this.errorMessage = 'Error al cargar roles: ' + error;
      
      // Fallback: crear roles de prueba para debugging
      this.roles = [
        { id: 1, nombre: 'ADMINISTRADOR' },
        { id: 2, nombre: 'TECNICO' },
        { id: 3, nombre: 'CLIENTE' }
      ];
      console.log('🔄 Usando roles de fallback');
    }
  });
}

// MÉTODO TEMPORAL PARA DEBUG
testTokenManually(): void {
  const token = localStorage.getItem('token');
  console.log('🧪 Testing token manually...');
  console.log('🔐 Token:', token);
  
  if (token) {
    // Test manual con fetch
    fetch('http://localhost:8080/api/admin/roles', {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    })
    .then(response => {
      console.log('📊 Response status:', response.status);
      return response.json();
    })
    .then(data => {
      console.log('📦 Response data:', data);
    })
    .catch(error => {
      console.error('💥 Fetch error:', error);
    });
  }
}

  cargarDatosUsuario(): void {
    if (!this.userId) return;

    this.isLoading = true;

    this.userService.obtenerUsuarioPorId(this.userId).subscribe({
      next: (response) => {
        this.isLoading = false;
        if (response.exito) {
          const usuario = response.datos;
          this.userForm.patchValue({
            nombre: usuario.nombre,
            apellido: usuario.apellido,
            mail: usuario.mail,
            rol: usuario.rol.id,
            estado: usuario.estado
          });
        } else {
          this.errorMessage = response.mensaje;
        }
      },
      error: (error) => {
        this.isLoading = false;
        this.errorMessage = error;
      }
    });
  }

  onSubmit(): void {
    if (this.userForm.valid) {
      this.isSubmitting = true;
      this.clearMessages();

      const formData = this.userForm.value;
      
      // Preparar datos para enviar
      const userData: UsuarioRequestDTO = {
        nombre: formData.nombre,
        apellido: formData.apellido,
        mail: formData.mail,
        contrasena: formData.contrasena,
        estado: formData.estado,
        rol: parseInt(formData.rol, 10)
      };

      // Si estamos en modo edición y no se proporcionó contraseña, no la enviamos
      if (this.isEditMode && !userData.contrasena) {
        delete (userData as any).contrasena;
      }

      const operation = this.isEditMode
        ? this.userService.actualizarUsuario(this.userId!, userData)
        : this.userService.crearUsuario(userData);

      operation.subscribe({
        next: (response) => {
          this.isSubmitting = false;
          if (response.exito) {
            this.successMessage = response.mensaje;
            // Redirigir después de 2 segundos
            setTimeout(() => {
              this.router.navigate(['/admin/usuarios']);
            }, 2000);
          } else {
            this.errorMessage = response.mensaje;
          }
        },
        error: (error) => {
          this.isSubmitting = false;
          this.errorMessage = error;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  markFormGroupTouched(): void {
    Object.keys(this.userForm.controls).forEach(key => {
      const control = this.userForm.get(key);
      control?.markAsTouched();
    });
  }

  volver(): void {
    this.router.navigate(['/admin/usuarios']);
  }

  clearMessages(): void {
    this.successMessage = '';
    this.errorMessage = '';
  }

  // Getters para acceso fácil a los controles
  get nombre() { return this.userForm.get('nombre'); }
  get apellido() { return this.userForm.get('apellido'); }
  get mail() { return this.userForm.get('mail'); }
  get contrasena() { return this.userForm.get('contrasena'); }
  get rol() { return this.userForm.get('rol'); }
  get estado() { return this.userForm.get('estado'); }

  getFieldErrorMessage(fieldName: string): string {
    const field = this.userForm.get(fieldName);
    
    if (field?.hasError('required')) {
      const fieldNames: { [key: string]: string } = {
        nombre: 'El nombre',
        apellido: 'El apellido',
        mail: 'El correo electrónico',
        contrasena: 'La contraseña',
        rol: 'El rol'
      };
      return `${fieldNames[fieldName]} es obligatorio`;
    }
    
    if (field?.hasError('email')) {
      return 'El formato del correo electrónico no es válido';
    }
    
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength']?.requiredLength;
      return `Debe tener al menos ${minLength} caracteres`;
    }
    
    return '';
  }
}