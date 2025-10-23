import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Cliente = () => import('@/entities/cliente/cliente.vue');
const ClienteUpdate = () => import('@/entities/cliente/cliente-update.vue');
const ClienteDetails = () => import('@/entities/cliente/cliente-details.vue');

const PlanDeCredito = () => import('@/entities/plan-de-credito/plan-de-credito.vue');
const PlanDeCreditoUpdate = () => import('@/entities/plan-de-credito/plan-de-credito-update.vue');
const PlanDeCreditoDetails = () => import('@/entities/plan-de-credito/plan-de-credito-details.vue');

const Cuenta = () => import('@/entities/cuenta/cuenta.vue');
const CuentaUpdate = () => import('@/entities/cuenta/cuenta-update.vue');
const CuentaDetails = () => import('@/entities/cuenta/cuenta-details.vue');

const MovimientoCrediticio = () => import('@/entities/movimiento-crediticio/movimiento-crediticio.vue');
const MovimientoCrediticioUpdate = () => import('@/entities/movimiento-crediticio/movimiento-crediticio-update.vue');
const MovimientoCrediticioDetails = () => import('@/entities/movimiento-crediticio/movimiento-crediticio-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'cliente',
      name: 'Cliente',
      component: Cliente,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/new',
      name: 'ClienteCreate',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/edit',
      name: 'ClienteEdit',
      component: ClienteUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cliente/:clienteId/view',
      name: 'ClienteView',
      component: ClienteDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan-de-credito',
      name: 'PlanDeCredito',
      component: PlanDeCredito,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan-de-credito/new',
      name: 'PlanDeCreditoCreate',
      component: PlanDeCreditoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan-de-credito/:planDeCreditoId/edit',
      name: 'PlanDeCreditoEdit',
      component: PlanDeCreditoUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'plan-de-credito/:planDeCreditoId/view',
      name: 'PlanDeCreditoView',
      component: PlanDeCreditoDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cuenta',
      name: 'Cuenta',
      component: Cuenta,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cuenta/new',
      name: 'CuentaCreate',
      component: CuentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cuenta/:cuentaId/edit',
      name: 'CuentaEdit',
      component: CuentaUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cuenta/:cuentaId/view',
      name: 'CuentaView',
      component: CuentaDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'movimiento-crediticio',
      name: 'MovimientoCrediticio',
      component: MovimientoCrediticio,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'movimiento-crediticio/new',
      name: 'MovimientoCrediticioCreate',
      component: MovimientoCrediticioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'movimiento-crediticio/:movimientoCrediticioId/edit',
      name: 'MovimientoCrediticioEdit',
      component: MovimientoCrediticioUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'movimiento-crediticio/:movimientoCrediticioId/view',
      name: 'MovimientoCrediticioView',
      component: MovimientoCrediticioDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
