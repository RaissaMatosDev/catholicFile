import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

export const adminGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const payload = authService.getPayload();
  const isAdmin = payload?.role === 'ADMINISTRADOR' ||
                  payload?.roles?.includes('ADMINISTRADOR') ||
                  payload?.authorities?.some((a: any) =>
                    a === 'ROLE_ADMINISTRADOR' || a?.authority === 'ROLE_ADMINISTRADOR'
                  );

  if (isAdmin) return true;

  router.navigate(['/dashboard']);
  return false;
};
