import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, LoginRequest } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      mail: ['', [Validators.required, Validators.email]],
      contrasena: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get mail() {
    return this.loginForm.get('mail');
  }

  get contrasena() {
    return this.loginForm.get('contrasena');
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isLoading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const credentials: LoginRequest = {
        mail: this.loginForm.value.mail,
        contrasena: this.loginForm.value.contrasena
      };

      this.authService.login(credentials).subscribe({
        next: (response) => {
          this.isLoading = false;
          this.successMessage = response.mensaje || 'Login exitoso';
          
          console.log('🎯 Rol del usuario logueado:', response.usuario.rol.nombre);
          
          // Mostrar mensaje de éxito brevemente antes de redirigir
          setTimeout(() => {
            // Redirigir según el rol del usuario (ROLES ACTUALIZADOS)
            const rolUsuario = response.usuario.rol.nombre.toUpperCase();
            
            switch (rolUsuario) {
              case 'ADMINISTRADOR':
                console.log('🔑 Redirigiendo a admin dashboard');
                this.router.navigate(['/admin/dashboard']);
                break;
              case 'TECNICO':
                console.log('🔧 Redirigiendo a dashboard de técnico');
                this.router.navigate(['/dashboard']); // Puedes crear una ruta específica para técnicos
                break;
              case 'CLIENTE':
                console.log('🙋‍♂️ Redirigiendo a dashboard de cliente');
                this.router.navigate(['/dashboard']);
                break;
              default:
                console.log('👤 Rol no reconocido, redirigiendo a dashboard general');
                this.router.navigate(['/dashboard']);
            }
          }, 1500);
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = error;
        }
      });
    } else {
      this.markFormGroupTouched();
    }
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      const control = this.loginForm.get(key);
      control?.markAsTouched();
    });
  }

  clearMessages(): void {
    this.errorMessage = '';
    this.successMessage = '';
  }

  hasFieldError(fieldName: string, errorType: string): boolean {
    const field = this.loginForm.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }

  getFieldErrorMessage(fieldName: string): string {
    const field = this.loginForm.get(fieldName);
    
    if (field?.hasError('required')) {
      return `${fieldName === 'mail' ? 'El correo electrónico' : 'La contraseña'} es obligatorio`;
    }
    
    if (field?.hasError('email')) {
      return 'El formato del correo electrónico no es válido';
    }
    
    if (field?.hasError('minlength')) {
      return 'La contraseña debe tener al menos 6 caracteres';
    }
    
    return '';
  }
}