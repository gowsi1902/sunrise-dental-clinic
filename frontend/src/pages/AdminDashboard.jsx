import { useEffect, useState } from 'react';
import api from '../api/client';
import { errorMessage, rupees } from '../components/format.jsx';

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [users, setUsers] = useState([]);
  const [logs, setLogs] = useState([]);
  const [error, setError] = useState('');
  const [form, setForm] = useState({ username: '', password: '', role: 'STAFF' });

  async function load() {
    const [s, u, a] = await Promise.all([
      api.get('/api/admin/stats'),
      api.get('/api/users'),
      api.get('/api/audit')
    ]);
    setStats(s.data.data);
    setUsers(u.data.data || []);
    setLogs(a.data.data || []);
  }

  useEffect(() => {
    load().catch((err) => setError(errorMessage(err, 'Could not load admin data')));
  }, []);

  async function createUser(e) {
    e.preventDefault();
    setError('');
    try {
      await api.post('/api/users', form);
      setForm({ username: '', password: '', role: 'STAFF' });
      await load();
    } catch (err) {
      setError(errorMessage(err));
    }
  }

  return (
    <div className="space-y-6">
      <header>
        <h1 className="font-display text-3xl text-clinic-900">Clinic administration</h1>
        <p className="text-sm text-slate-500">Daily counts, staff accounts and activity trail.</p>
      </header>
      {error && <p className="rounded-lg bg-red-50 text-red-700 px-3 py-2 text-sm">{error}</p>}
      {stats && (
        <section className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <Stat label="Appointments" value={stats.totalAppointments} />
          <Stat label="Today (scheduled)" value={stats.todayCount} />
          <Stat label="Expected revenue" value={rupees(stats.expectedRevenue)} />
          <Stat label="Collected" value={rupees(stats.collectedRevenue)} />
          <Stat label="Scheduled" value={stats.scheduledCount} />
          <Stat label="Completed" value={stats.completedCount} />
          <Stat label="Cancelled" value={stats.cancelledCount} />
        </section>
      )}
      <section className="grid lg:grid-cols-2 gap-6">
        <div className="card p-6">
          <h2 className="font-semibold mb-4">Staff accounts</h2>
          <form onSubmit={createUser} className="grid gap-3 mb-4">
            <input className="field" placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
            <input className="field" type="password" placeholder="Password (min 6)" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
            <select className="field" value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option value="STAFF">Staff</option>
              <option value="ADMIN">Admin</option>
            </select>
            <button className="btn-primary" type="submit">Add account</button>
          </form>
          <ul className="text-sm divide-y">
            {users.map((u) => (
              <li key={u.id} className="py-2 flex justify-between">
                <span>{u.username}</span>
                <span className="uppercase text-xs text-slate-500">{u.role}</span>
              </li>
            ))}
          </ul>
        </div>
        <div className="card p-6">
          <h2 className="font-semibold mb-4">Audit trail</h2>
          <ul className="text-sm space-y-3 max-h-[420px] overflow-auto">
            {logs.map((log) => (
              <li key={log.id} className="border-b border-slate-100 pb-2">
                <p className="font-medium">{log.username} · {log.action}</p>
                <p className="text-slate-500">{log.details}</p>
                <p className="text-xs text-slate-400">{log.timestamp?.replace('T', ' ')}</p>
              </li>
            ))}
          </ul>
        </div>
      </section>
    </div>
  );
}

function Stat({ label, value }) {
  return (
    <div className="card p-4">
      <p className="text-xs uppercase tracking-wide text-slate-500">{label}</p>
      <p className="text-xl font-semibold text-clinic-900 mt-1">{value}</p>
    </div>
  );
}
