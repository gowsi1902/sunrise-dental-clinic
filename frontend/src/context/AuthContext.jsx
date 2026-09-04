import { createContext, useContext, useMemo, useState } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('sunrise-user');
    return stored ? JSON.parse(stored) : null;
  });

  const value = useMemo(
    () => ({
      user,
      isAdmin: user?.role === 'ADMIN',
      async login(username, password) {
        const { data } = await api.post('/api/login', { username, password });
        localStorage.setItem('sunrise-user', JSON.stringify(data.data));
        setUser(data.data);
        return data.data;
      },
      async logout() {
        try {
          await api.post('/api/logout');
        } finally {
          localStorage.removeItem('sunrise-user');
          setUser(null);
        }
      }
    }),
    [user]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
