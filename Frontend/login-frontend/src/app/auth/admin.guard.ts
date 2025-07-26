import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
export const AdminGuard = () => {
  const router = inject(Router);
  const authService = inject(AuthService);

  if (authService.isAuthenticated() && authService.isAdmin()) {
    return true;
  }

  router.navigate(['/user/dashboard']);
  return false;
};