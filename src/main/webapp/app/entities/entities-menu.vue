<template>
  <div>
    <b-dropdown-item to="/cliente">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.cliente')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/plan-de-credito">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.planDeCredito')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/cuenta">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.cuenta')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/movimiento-crediticio">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.movimientoCrediticio')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
