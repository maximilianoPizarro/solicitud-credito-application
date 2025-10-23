import { type TipoPlan } from '@/shared/model/enumerations/tipo-plan.model';
export interface IPlanDeCredito {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  tipo?: keyof typeof TipoPlan;
  tasaInteres?: number;
  plazoMaximo?: number | null;
}

export class PlanDeCredito implements IPlanDeCredito {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
    public tipo?: keyof typeof TipoPlan,
    public tasaInteres?: number,
    public plazoMaximo?: number | null,
  ) {}
}
