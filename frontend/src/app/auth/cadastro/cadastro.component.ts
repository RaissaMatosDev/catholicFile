import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

function senhasIguais(group: AbstractControl): ValidationErrors | null {
  const senha = group.get('senha')?.value;
  const confirmar = group.get('confirmarSenha')?.value;
  return senha && confirmar && senha !== confirmar ? { senhasDiferentes: true } : null;
}

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss']
})
export class CadastroComponent {
  form: FormGroup;
  loading = false;
  erro: string | null = null;
  sucesso = false;
  mostrarSenha = false;
  mostrarConfirmar = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      confirmarSenha: ['', Validators.required]
    }, { validators: senhasIguais });
  }

  get nome() { return this.form.get('nome')!; }
  get email() { return this.form.get('email')!; }
  get senha() { return this.form.get('senha')!; }
  get confirmarSenha() { return this.form.get('confirmarSenha')!; }

  get forcaSenha(): number {
    const v = this.senha.value || '';
    let score = 0;
    if (v.length >= 6) score++;
    if (v.length >= 10) score++;
    if (/[A-Z]/.test(v)) score++;
    if (/[0-9]/.test(v)) score++;
    if (/[^a-zA-Z0-9]/.test(v)) score++;
    return score;
  }

  get labelForca(): string {
    const labels = ['', 'Fraca', 'Razoável', 'Boa', 'Forte', 'Excelente'];
    return labels[this.forcaSenha] || '';
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.loading = true;
    this.erro = null;

    const { nome, email, senha } = this.form.value;
    this.authService.cadastrar({ nome, email, senha }).subscribe({
      next: () => {
        this.sucesso = true;
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err) => {
        this.loading = false;
        this.erro = err.status === 409
          ? 'Este e-mail já está cadastrado.'
          : 'Erro ao criar conta. Tente novamente.';
      }
    });
  }
}
