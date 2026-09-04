import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

const linkClass = ({ isActive }) =>
  `block rounded-lg px-3 py-2 text-sm font-medium ${
    isActive ? 'bg-clinic-700 text-white' : 'text-slate-200 hover:bg-clinic-700/60'
  }`;

export default function Layout() {
  const { user, isAdmin, logout } = useAuth();
  const navigate = useNavigate();

  async function exitSystem() {
    await logout();
    navigate('/login');
  }

  return (
    <div className="min-h-screen grid md:grid-cols-[240px_1fr]">
      <aside className="bg-clinic-900 text-white p-5 flex flex-col">
        <p className="font-display text-xl leading-tight">Sunrise Dental</p>
        <p className="text-sky-200 text-xs tracking-[0.18em] uppercase mt-1">Colombo clinic</p>
        <nav className="mt-8 space-y-1 flex-1">
          <NavLink to="/" end className={linkClass}>
            Appointments
          </NavLink>
          <NavLink to="/appointments/new" className={linkClass}>
            Register visit
          </NavLink>
          <NavLink to="/help" className={linkClass}>
            Help
          </NavLink>
          {isAdmin && (
            <NavLink to="/admin" className={linkClass}>
              Administration
            </NavLink>
          )}
        </nav>
        <div className="text-sm text-slate-300">
          <p className="font-semibold text-white">{user?.username}</p>
          <p className="uppercase text-xs tracking-wide text-sky-200">{user?.role}</p>
          <button type="button" onClick={exitSystem} className="mt-3 text-xs underline text-slate-300">
            Exit system
          </button>
        </div>
      </aside>
      <main className="p-6 md:p-8">
        <Outlet />
      </main>
    </div>
  );
}
