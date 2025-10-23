import { type ICliente } from '@/shared/model/cliente.model';
import { type IPlanDeCredito } from '@/shared/model/plan-de-credito.model';

import { type EstadoCuenta } from '@/shared/model/enumerations/estado-cuenta.model';
export interface ICuenta {
  id?: number;
  numeroCuenta?: string;
  fechaApertura?: Date;
  montoOtorgado?: number;
  saldoPendiente?: number | null;
  estado?: keyof typeof EstadoCuenta;
  fechaCierre?: Date | null;
  cliente?: ICliente | null;
  plan?: IPlanDeCredito | null;
}

export class Cuenta implements ICuenta {
  constructor(
    public id?: number,
    public numeroCuenta?: string,
    public fechaApertura?: Date,
    public montoOtorgado?: number,
    public saldoPendiente?: number | null,
    public estado?: keyof typeof EstadoCuenta,
    public fechaCierre?: Date | null,
    public cliente?: ICliente | null,
    public plan?: IPlanDeCredito | null,
  ) {}
}
