import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to add the JWT token to headers
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Handle unauthorized (e.g., redirect to login)
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (!window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: (credentials) => api.post('/auth/login', credentials),
  register: (userData) => api.post('/auth/register', userData),
};

export const leaveService = {
  applyLeave: (leaveData) => api.post('/leaves/apply', leaveData),
  getMyLeaves: () => api.get('/leaves/my-leaves'),
  getBalance: () => api.get('/leaves/balance'),
  cancelLeave: (id) => api.delete(`/leaves/${id}`),
};

export const adminService = {
  getPendingLeaves: () => api.get('/admin/leaves/pending'),
  approveLeave: (id) => api.put(`/admin/leaves/${id}/approve`),
  rejectLeave: (id, commentData) => api.put(`/admin/leaves/${id}/reject`, commentData),
  getEmployeeSummaries: () => api.get('/admin/employees/summaries'), // Verify this path in backend
  getReportsSummary: () => api.get('/admin/reports/summary'),
  getAllLeaves: (params) => api.get('/admin/leaves/all', { params }),
};

export default api;
