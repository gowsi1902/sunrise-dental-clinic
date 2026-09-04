import { useEffect, useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/client';
import { errorMessage, rupees, statusBadge } from '../components/format.jsx';

export default function Dashboard() {
  const [rows, setRows] = useState([]);
  const [query, setQuery] = useState('');
  const [number, setNumber] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    api
      .get('/api/appointments')
      .then((res) => setRows(res.data.data || []))
      .catch((err) => setError(errorMessage(err, 'Could not load appointments')));
  }, []);

  const filtered = useMemo(() => {
    const q = query.toLowerCase();
    return rows.filter((row) =>
      `${row.appointmentNo} ${row.patientName} ${row.dentistName} ${row.contactNumber}`.toLowerCase().includes(q)
    );
  }, [rows, query]);

  async function searchNumber(e) {
    e.preventDefault();
    setError('');
    try {
      const { data } = await api.get('/api/appointments', { params: { number } });
      navigate(`/appointments/${data.data.id}`);
    } catch (err) {
      setError(errorMessage(err, 'Appointment not found'));
    }
  }

  return (
    <div className="space-y-6">
      <header className="flex flex-wrap items-end justify-between gap-4">
        <div>
          <h1 className="font-display text-3xl text-clinic-900">Appointments</h1>
          <p className="text-slate-500 text-sm">Sunrise Dental Clinic, Colombo.</p>
        </div>
        <Link to="/appointments/new" className="btn-primary">
          Register new appointment
        </Link>
      </header>

      <form onSubmit={searchNumber} className="card p-4 flex flex-wrap gap-3 items-end">
        <label className="text-sm font-medium flex-1 min-w-[200px]">
          Search by appointment number
          <input className="field mt-1" placeholder="SDC-0001" value={number} onChange={(e) => setNumber(e.target.value)} />
        </label>
        <button className="btn-primary" type="submit">
          Display details
        </button>
        <input
          className="field max-w-sm"
          placeholder="Filter list by patient or dentist"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
      </form>

      {error && <p className="text-red-700 text-sm">{error}</p>}

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead className="bg-clinic-50 text-left text-slate-600">
            <tr>
              <th className="px-4 py-3">No.</th>
              <th className="px-4 py-3">Patient</th>
              <th className="px-4 py-3">Dentist / treatment</th>
              <th className="px-4 py-3">When</th>
              <th className="px-4 py-3">Bill</th>
              <th className="px-4 py-3">Status</th>
              <th className="px-4 py-3" />
            </tr>
          </thead>
          <tbody>
            {filtered.map((row) => (
              <tr key={row.id} className="border-t border-slate-100">
                <td className="px-4 py-3 font-mono text-xs">{row.appointmentNo}</td>
                <td className="px-4 py-3">
                  <p className="font-semibold">{row.patientName}</p>
                  <p className="text-xs text-slate-500">{row.contactNumber}</p>
                </td>
                <td className="px-4 py-3">
                  {row.dentistName}
                  <p className="text-xs text-slate-500">{row.treatmentName}</p>
                </td>
                <td className="px-4 py-3">
                  {row.appointmentDate} {String(row.appointmentTime).slice(0, 5)}
                </td>
                <td className="px-4 py-3">{rupees(row.totalAmount)}</td>
                <td className="px-4 py-3">{statusBadge(row.status)}</td>
                <td className="px-4 py-3 text-right">
                  <Link className="text-clinic-700 font-semibold" to={`/appointments/${row.id}`}>
                    Open
                  </Link>
                </td>
              </tr>
            ))}
            {filtered.length === 0 && (
              <tr>
                <td colSpan="7" className="px-4 py-10 text-center text-slate-500">
                  No appointments to show.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
