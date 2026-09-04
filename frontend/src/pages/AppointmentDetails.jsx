import { useEffect, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import api from '../api/client';
import { errorMessage, rupees, statusBadge } from '../components/format.jsx';

export default function AppointmentDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [row, setRow] = useState(null);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function load() {
    const { data } = await api.get(`/api/appointments/${id}`);
    setRow(data.data);
  }

  useEffect(() => {
    load().catch((err) => setError(errorMessage(err, 'Could not load appointment')));
  }, [id]);

  async function setStatus(status) {
    setBusy(true);
    setError('');
    try {
      await api.put(`/api/appointments/${id}/status`, { status });
      await load();
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setBusy(false);
    }
  }

  async function remove() {
    if (!window.confirm('Delete this appointment permanently?')) {
      return;
    }
    setBusy(true);
    try {
      await api.delete(`/api/appointments/${id}`);
      navigate('/');
    } catch (err) {
      setError(errorMessage(err));
      setBusy(false);
    }
  }

  if (!row && !error) {
    return <p>Loading…</p>;
  }

  return (
    <div className="max-w-3xl space-y-6">
      <header className="flex flex-wrap items-start justify-between gap-4">
        <div>
          <p className="text-xs uppercase tracking-widest text-slate-500">{row?.appointmentNo}</p>
          <h1 className="font-display text-3xl text-clinic-900">{row?.patientName}</h1>
          {row && statusBadge(row.status)}
        </div>
        <div className="flex flex-wrap gap-2">
          <Link className="btn-ghost" to={`/appointments/${id}/edit`}>
            Edit
          </Link>
          <Link className="btn-primary" to={`/billing/${id}`}>
            Calculate / print bill
          </Link>
        </div>
      </header>
      {error && <p className="rounded-lg bg-red-50 text-red-700 px-3 py-2 text-sm">{error}</p>}
      {row && (
        <section className="card p-6 grid md:grid-cols-2 gap-4 text-sm">
          <p>
            <span className="text-slate-500 block">Address</span>
            {row.address}
          </p>
          <p>
            <span className="text-slate-500 block">Contact</span>
            {row.contactNumber}
          </p>
          <p>
            <span className="text-slate-500 block">Dentist</span>
            {row.dentistName}
          </p>
          <p>
            <span className="text-slate-500 block">Treatment type</span>
            {row.treatmentName}
          </p>
          <p>
            <span className="text-slate-500 block">Date and time</span>
            {row.appointmentDate} at {String(row.appointmentTime).slice(0, 5)}
          </p>
          <p>
            <span className="text-slate-500 block">Estimated total</span>
            {rupees(row.totalAmount)}
          </p>
        </section>
      )}
      <section className="flex flex-wrap gap-2">
        <button className="btn-ghost" disabled={busy} onClick={() => setStatus('COMPLETED')}>
          Mark completed
        </button>
        <button className="btn-ghost" disabled={busy} onClick={() => setStatus('CANCELLED')}>
          Cancel appointment
        </button>
        <button className="btn-ghost text-red-700 border-red-200" disabled={busy} onClick={remove}>
          Delete
        </button>
      </section>
    </div>
  );
}
