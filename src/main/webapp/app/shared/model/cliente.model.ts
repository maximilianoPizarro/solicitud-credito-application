import { type TipoDocumento } from '@/shared/model/enumerations/tipo-documento.model';
export interface ICliente {
  id?: number;
  numeroCliente?: number;
  nombre?: string;
  apellido?: string;
  fechaNacimiento?: Date;
  tipoDocumento?: keyof typeof TipoDocumento;
  numeroDocumento?: string;
  email?: string | null;
  telefono?: string | null;
}

export class Cliente implements ICliente {
  constructor(
    public id?: number,
    public numeroCliente?: number,
    public nombre?: string,
    public apellido?: string,
    public fechaNacimiento?: Date,
    public tipoDocumento?: keyof typeof TipoDocumento,
    public numeroDocumento?: string,
    public email?: string | null,
    public telefono?: string | null,
  ) {}
}
