import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  form: FormGroup;
  loading = false;
  erro: string | null = null;
  mostrarSenha = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get email() { return this.form.get('email')!; }
  get senha() { return this.form.get('senha')!; }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;
    this.erro = null;

    this.authService.login(this.form.value).subscribe({
      next: (res) => {
        if (res?.token) this.authService.salvarToken(res.token);
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.loading = false;
        this.erro = err.status === 401
          ? 'E-mail ou senha incorretos.'
          : 'Erro ao conectar. Tente novamente.';
      }
    });
  }
}
