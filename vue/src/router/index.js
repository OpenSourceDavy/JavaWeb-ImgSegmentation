import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:"/img",
    children:[
      {
        path: 'person',
        name: 'Person',
        component: () => import('../views/Person.vue'),
      },
      {
        path: 'patient',
        name: 'Patient',
        component: () => import('../views/Patient.vue'),
      },
      {
        path: 'specialist',
        name: 'Specialist',
        component: () => import('../views/Specialist.vue'),
      },
      {
        path: 'img',
        name: 'Img',
        component: () => import('../views/Img.vue'),
      },
    ]
  },
  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/Login.vue')
  },
  {
    path: '/canvas',
    name: 'Canvas',
    component: () => import(/* webpackChunkName: "about" */ '../views/Canvas.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
