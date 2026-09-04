import { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import api from '../api/client';
import { errorMessage, rupees, statusBadge } from '../components/format.jsx';

export default function Billing() {
  const { id } = useParams();
  const [bill, setBill] = useState(null);
  const [error, setError] = useState('');
  const [method, setMethod] = useState('CASH');
  const [busy, setBusy] = useState(false);

  async function load() {
    const { data } = await api.get(`/api/billing/${id}`);
    setBill(data.data);
  }

  useEffect(() => {
    load().catch((err) => setError(errorMessage(err, 'Could not load bill')));
  }, [id]);

  async function collect() {
    setBusy(true);
    setError('');
    try {
      await api.post(`/api/billing/${id}/pay`, { paymentMethod: method });
      await load();
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setBusy(false);
    }
  }

  if (!bill && !error) {
    return <p>Preparing receipt…</p>;
  }

  return (
    <div className="max-w-2xl space-y-6">
      <div className="flex justify-between items-center print:hidden">
        <Link to={`/appointments/${id}`} className="text-sm text-clinic-700">
          ← Back to appointment
        </Link>
        <button className="btn-ghost" onClick={() => window.print()}>
          Print bill
        </button>
      </div>
      {error && <p className="rounded-lg bg-red-50 text-red-700 px-3 py-2 text-sm print:hidden">{error}</p>}
      {bill && (
        <article className="card p-8 space-y-6">
          <header className="flex justify-between gap-4 border-b border-slate-200 pb-4">
            <div>
              <p className="font-display text-2xl text-clinic-900">Sunrise Dental Clinic</p>
              <p className="text-sm text-slate-500">Colombo, Sri Lanka</p>
            </div>
            <div className="text-right text-sm">
              <p>Receipt {bill.appointmentNo}</p>
              {statusBadge(bill.status)}
            </div>
          </header>
          <section className="grid md:grid-cols-2 gap-4 text-sm">
            <p>
              <span className="text-slate-500 block">Patient</span>
              {bill.patientName}
              <br />
              {bill.address}
              <br />
              {bill.contactNumber}
            </p>
            <p>
              <span className="text-slate-500 block">Visit</span>
              {bill.dentistName}
              <br />
              {bill.treatmentName}
              <br />
              {bill.appointmentDate} {String(bill.appointmentTime).slice(0, 5)}
            </p>
          </section>
          <table className="w-full text-sm">
            <tbody>
              <tr className="border-t">
                <td className="py-2">Treatment fee</td>
                <td className="py-2 text-right">{rupees(bill.treatmentFee)}</td>
              </tr>
              <tr>
                <td className="py-2">Consultation fee</td>
                <td className="py-2 text-right">{rupees(bill.consultationFee)}</td>
              </tr>
              <tr className="font-semibold border-t">
                <td className="py-2">Total</td>
                <td className="py-2 text-right">{rupees(bill.total)}</td>
              </tr>
              <tr>
                <td className="py-2">Paid</td>
                <td className="py-2 text-right">{rupees(bill.amountPaid)}</td>
              </tr>
              <tr>
                <td className="py-2">Balance due</td>
                <td className="py-2 text-right">{rupees(bill.balanceDue)}</td>
              </tr>
            </tbody>
          </table>
        </article>
      )}
      {bill && Number(bill.balanceDue) > 0 && bill.status !== 'CANCELLED' && (
        <div className="card p-4 flex flex-wrap gap-3 items-end print:hidden">
          <label className="text-sm font-medium">
            Method
            <select className="field mt-1" value={method} onChange={(e) => setMethod(e.target.value)}>
              <option value="CASH">Cash</option>
              <option value="CARD">Card</option>
            </select>
          </label>
          <button className="btn-primary" disabled={busy} onClick={collect}>
            Collect balance
          </button>
        </div>
      )}
    </div>
  );
}
