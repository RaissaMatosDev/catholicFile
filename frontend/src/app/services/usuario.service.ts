import { tap } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LoginRequestDTO {
  email: string;
  senha: string;
}

export interface LoginResponseDTO {
  token: string;
  type: string;
}

export interface UsuarioDTO {
  id: number;
  nome: string;
  email: string;
}

export interface UsuarioCadastroDTO {
  nome: string;
  email: string;
  senha: string;
}

export interface UsuarioAttDTO {
  nome?: string;
  email?: string;
  senha?: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {

  private apiUrl = '/usuarios';

  constructor(private http: HttpClient) {}


  login(dados: LoginRequestDTO): Observable<LoginResponseDTO> {
    return this.http.post<LoginResponseDTO>(`${this.apiUrl}/login`, dados).pipe(
      tap(response => {
        localStorage.setItem('token', response.token);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }


  cadastrar(dados: UsuarioCadastroDTO): Observable<UsuarioDTO> {
    return this.http.post<UsuarioDTO>(`${this.apiUrl}/usuarios`, dados);
  }


  listar(page = 0, size = 10): Observable<any> {
    return this.http.get(`${this.apiUrl}?page=${page}&size=${size}`);
  }


  atualizarMeuPerfil(dados: UsuarioAttDTO): Observable<UsuarioDTO> {
    return this.http.put<UsuarioDTO>(`${this.apiUrl}/me`, dados);
  }


  atualizarPorId(id: number, dados: UsuarioAttDTO): Observable<UsuarioDTO> {
    return this.http.put<UsuarioDTO>(`${this.apiUrl}/${id}`, dados);
  }


  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
