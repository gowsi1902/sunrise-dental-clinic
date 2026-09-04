export function statusBadge(status) {
  const styles = {
    SCHEDULED: 'bg-amber-100 text-amber-800',
    COMPLETED: 'bg-emerald-100 text-emerald-800',
    CANCELLED: 'bg-slate-200 text-slate-600'
  };
  return <span className={`badge ${styles[status] || 'bg-slate-100'}`}>{status}</span>;
}

export function rupees(value) {
  const amount = Number(value || 0);
  return `LKR ${amount.toLocaleString('en-LK', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

export function errorMessage(err, fallback = 'Request failed') {
  return err.response?.data?.message || fallback;
}

export const TIME_SLOTS = [
  '09:00', '09:30', '10:00', '10:30', '11:00', '11:30',
  '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30'
];
