import { Navigate, Route, Routes } from 'react-router-dom';
import { useAuth } from './context/AuthContext.jsx';
import Layout from './components/Layout.jsx';
import Login from './pages/Login.jsx';
import Dashboard from './pages/Dashboard.jsx';
import AddAppointment from './pages/AddAppointment.jsx';
import AppointmentDetails from './pages/AppointmentDetails.jsx';
import Billing from './pages/Billing.jsx';
import Help from './pages/Help.jsx';
import AdminDashboard from './pages/AdminDashboard.jsx';

function Guard({ children, adminOnly = false }) {
  const { user, isAdmin } = useAuth();
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  if (adminOnly && !isAdmin) {
    return <Navigate to="/" replace />;
  }
  return children;
}

export default function App() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={user ? <Navigate to="/" replace /> : <Login />} />
      <Route
        element={
          <Guard>
            <Layout />
          </Guard>
        }
      >
        <Route path="/" element={<Dashboard />} />
        <Route path="/appointments/new" element={<AddAppointment />} />
        <Route path="/appointments/:id/edit" element={<AddAppointment />} />
        <Route path="/appointments/:id" element={<AppointmentDetails />} />
        <Route path="/billing/:id" element={<Billing />} />
        <Route path="/help" element={<Help />} />
        <Route
          path="/admin"
          element={
            <Guard adminOnly>
              <AdminDashboard />
            </Guard>
          }
        />
      </Route>
      <Route path="*" element={<Navigate to={user ? '/' : '/login'} replace />} />
    </Routes>
  );
}
