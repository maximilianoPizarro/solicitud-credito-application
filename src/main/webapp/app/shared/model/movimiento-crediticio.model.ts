import { type ICuenta } from '@/shared/model/cuenta.model';

import { type TipoMovimiento } from '@/shared/model/enumerations/tipo-movimiento.model';
export interface IMovimientoCrediticio {
  id?: number;
  fechaMovimiento?: Date;
  tipo?: keyof typeof TipoMovimiento;
  monto?: number;
  descripcion?: string | null;
  referenciaExterna?: string | null;
  cuenta?: ICuenta | null;
}

export class MovimientoCrediticio implements IMovimientoCrediticio {
  constructor(
    public id?: number,
    public fechaMovimiento?: Date,
    public tipo?: keyof typeof TipoMovimiento,
    public monto?: number,
    public descripcion?: string | null,
    public referenciaExterna?: string | null,
    public cuenta?: ICuenta | null,
  ) {}
}
