import axios from 'axios';

// 创建axios实例
const request = axios.create({
  baseURL: '/api/form', // 通过Vite代理到后端
  timeout: 5000
});

export default request;