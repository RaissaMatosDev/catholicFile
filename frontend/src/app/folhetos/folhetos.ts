import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FolhetoService, FolhetoDTO } from '../services/folheto.service';
import { AuthService } from '../services/auth.service';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-folhetos',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './folhetos.html',
  styleUrls: ['./folhetos.scss']
})
export class Folhetos implements OnInit {
  folhetos: FolhetoDTO[] = [];
  carregando = true;
  erro: string | null = null;
  isAdmin = false;
  paginaAtual = 0;
  totalPaginas = 0;
  totalElementos = 0;
  tamanhoPagina = 10;

  constructor(
    private folhetoService: FolhetoService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const payload = this.authService.getPayload();
    this.isAdmin = payload?.roles?.includes('ROLE_ADMINISTRADOR') ?? false;
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.erro = null;
    this.folhetoService.listar(this.paginaAtual, this.tamanhoPagina).subscribe({
      next: (page) => {
        this.folhetos = page.content;
        this.totalPaginas = page.totalPages;
        this.totalElementos = page.totalElements;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.erro = 'Erro ao carregar folhetos.';
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }

  abrirPdf(id: number): void {
    const token = this.authService.getToken();
    fetch(`${environment.apiUrl}/folheto/${id}/pdf`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(res => res.blob())
    .then(blob => {
      const url = URL.createObjectURL(blob);
      window.open(url, '_blank');
    });
  }

  paginaAnterior(): void {
    if (this.paginaAtual > 0) {
      this.paginaAtual--;
      this.carregar();
    }
  }

  proximaPagina(): void {
    if (this.paginaAtual < this.totalPaginas - 1) {
      this.paginaAtual++;
      this.carregar();
    }
  }

  excluir(id: number): void {
    if (!confirm('Deseja excluir este folheto?')) return;
    this.folhetoService.excluir(id).subscribe({
      next: () => this.carregar(),
      error: () => this.erro = 'Erro ao excluir folheto.'
    });
  }

  voltar(): void {
    this.router.navigate(['/dashboard']);
  }
}
