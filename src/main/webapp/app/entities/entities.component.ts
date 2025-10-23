import { defineComponent, provide } from 'vue';

import ClienteService from './cliente/cliente.service';
import PlanDeCreditoService from './plan-de-credito/plan-de-credito.service';
import CuentaService from './cuenta/cuenta.service';
import MovimientoCrediticioService from './movimiento-crediticio/movimiento-crediticio.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('clienteService', () => new ClienteService());
    provide('planDeCreditoService', () => new PlanDeCreditoService());
    provide('cuentaService', () => new CuentaService());
    provide('movimientoCrediticioService', () => new MovimientoCrediticioService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
