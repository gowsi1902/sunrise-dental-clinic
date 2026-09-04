import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { errorMessage } from '../components/format.jsx';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function submit(e) {
    e.preventDefault();
    setBusy(true);
    setError('');
    try {
      await login(form.username, form.password);
      navigate('/');
    } catch (err) {
      setError(errorMessage(err, 'Could not sign in'));
    } finally {
      setBusy(false);
    }
  }

  return (
    <div className="min-h-screen grid md:grid-cols-2">
      <section className="hidden md:flex flex-col justify-between bg-clinic-900 text-white p-12">
        <div>
          <p className="text-sky-200 tracking-[0.3em] uppercase text-xs">Colombo</p>
          <h1 className="font-display text-5xl mt-4 leading-tight">Sunrise Dental Clinic</h1>
        </div>
        <p className="max-w-md text-slate-200 text-lg">
          Reception workspace for patient registration, dentist appointments and treatment billing.
        </p>
      </section>
      <section className="flex items-center justify-center p-8">
        <form onSubmit={submit} className="card w-full max-w-md p-8 space-y-5">
          <div>
            <h2 className="font-display text-2xl text-clinic-900">Staff login</h2>
            <p className="text-sm text-slate-500 mt-1">Only authorised clinic staff may continue.</p>
          </div>
          {error && <p className="rounded-lg bg-red-50 text-red-700 text-sm px-3 py-2">{error}</p>}
          <label className="block text-sm font-medium">
            Username
            <input
              className="field mt-1"
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
              autoComplete="username"
              required
            />
          </label>
          <label className="block text-sm font-medium">
            Password
            <input
              type="password"
              className="field mt-1"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              autoComplete="current-password"
              required
            />
          </label>
          <button className="btn-primary w-full" disabled={busy}>
            {busy ? 'Checking…' : 'Sign in'}
          </button>
        </form>
      </section>
    </div>
  );
}
