import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface FolhetoDTO {
  id?: number;
  titulo: string;
  lit?: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class FolhetoService {

  private apiUrl = `${environment.apiUrl}/folheto`;

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<FolhetoDTO>> {
    return this.http.get<PageResponse<FolhetoDTO>>(
      `${this.apiUrl}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<FolhetoDTO> {
    return this.http.get<FolhetoDTO>(`${this.apiUrl}/${id}`);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
