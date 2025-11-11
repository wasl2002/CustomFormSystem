import { createRouter, createWebHistory } from 'vue-router';
import FormList from '../views/FormList.vue';
import FormDesigner from '../views/FormDesigner.vue';
import FormDataManage from '../views/FormDataManage.vue';
import FormStatistics from '../views/FormStatistics.vue';

const routes = [
  {
    path: '/',
    name: 'Home',
    redirect: '/form/list'
  },
  {
    path: '/form/list',
    name: 'FormList',
    component: FormList
  },
  {
    path: '/form/designer',
    name: 'FormDesigner',
    component: FormDesigner
  },
  {
    path: '/form/designer/:id',
    name: 'FormDesignerEdit',
    component: FormDesigner,
    props: true
  },
  {
    path: '/form/data/:formKey',
    name: 'FormDataManage',
    component: FormDataManage,
    props: true
  },
  {
    path: '/form/statistics/:formKey',
    name: 'FormStatistics',
    component: FormStatistics,
    props: true
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
