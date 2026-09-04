import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import api from '../api/client';
import { errorMessage, rupees, TIME_SLOTS } from '../components/format.jsx';

const schema = Yup.object({
  patientName: Yup.string().required('Patient name is required'),
  address: Yup.string().required('Address is required'),
  contactNumber: Yup.string().required('Contact number is required'),
  dentistId: Yup.number().moreThan(0, 'Select a dentist'),
  treatmentId: Yup.number().moreThan(0, 'Select a treatment'),
  appointmentDate: Yup.string().required('Date is required'),
  appointmentTime: Yup.string().required('Time is required')
});

const empty = {
  patientName: '',
  address: '',
  contactNumber: '',
  dentistId: 0,
  treatmentId: 0,
  appointmentDate: '',
  appointmentTime: ''
};

export default function AddAppointment() {
  const { id } = useParams();
  const editing = Boolean(id);
  const navigate = useNavigate();
  const [dentists, setDentists] = useState([]);
  const [treatments, setTreatments] = useState([]);
  const [initial, setInitial] = useState(empty);
  const [serverError, setServerError] = useState('');
  const [loading, setLoading] = useState(editing);

  useEffect(() => {
    Promise.all([api.get('/api/dentists'), api.get('/api/treatments')]).then(([d, t]) => {
      setDentists(d.data.data || []);
      setTreatments(t.data.data || []);
    });
  }, []);

  useEffect(() => {
    if (!editing) {
      return;
    }
    api
      .get(`/api/appointments/${id}`)
      .then((res) => {
        const r = res.data.data;
        setInitial({
          patientName: r.patientName,
          address: r.address,
          contactNumber: r.contactNumber,
          dentistId: r.dentistId,
          treatmentId: r.treatmentId,
          appointmentDate: r.appointmentDate,
          appointmentTime: String(r.appointmentTime).slice(0, 5)
        });
      })
      .catch((err) => setServerError(errorMessage(err)))
      .finally(() => setLoading(false));
  }, [editing, id]);

  if (loading) {
    return <p>Loading appointment…</p>;
  }

  return (
    <div className="max-w-3xl space-y-6">
      <header>
        <h1 className="font-display text-3xl text-clinic-900">
          {editing ? 'Update appointment' : 'Register new appointment'}
        </h1>
        <p className="text-sm text-slate-500">A unique appointment number is issued automatically.</p>
      </header>
      {serverError && <p className="rounded-lg bg-red-50 text-red-700 px-3 py-2 text-sm">{serverError}</p>}
      <Formik
        enableReinitialize
        initialValues={initial}
        validationSchema={schema}
        onSubmit={async (values, helpers) => {
          setServerError('');
          try {
            const payload = {
              ...values,
              dentistId: Number(values.dentistId),
              treatmentId: Number(values.treatmentId)
            };
            if (editing) {
              await api.put(`/api/appointments/${id}`, payload);
              navigate(`/appointments/${id}`);
            } else {
              const { data } = await api.post('/api/appointments', payload);
              navigate(`/appointments/${data.data.id}`);
            }
          } catch (err) {
            setServerError(errorMessage(err, 'Could not save appointment'));
            helpers.setSubmitting(false);
          }
        }}
      >
        {({ isSubmitting }) => (
          <Form className="card p-6 grid gap-4 md:grid-cols-2">
            <label className="text-sm font-medium md:col-span-2">
              Patient name
              <Field name="patientName" className="field mt-1" />
              <ErrorMessage name="patientName" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium md:col-span-2">
              Address
              <Field name="address" className="field mt-1" />
              <ErrorMessage name="address" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium">
              Contact number
              <Field name="contactNumber" className="field mt-1" />
              <ErrorMessage name="contactNumber" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium">
              Dentist
              <Field as="select" name="dentistId" className="field mt-1">
                <option value={0}>Select dentist</option>
                {dentists.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.fullName} · {d.specialization}
                  </option>
                ))}
              </Field>
              <ErrorMessage name="dentistId" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium md:col-span-2">
              Treatment type
              <Field as="select" name="treatmentId" className="field mt-1">
                <option value={0}>Select treatment</option>
                {treatments.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.name} · treatment {rupees(t.treatmentFee)} + consult {rupees(t.consultationFee)}
                  </option>
                ))}
              </Field>
              <ErrorMessage name="treatmentId" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium">
              Date
              <Field type="date" name="appointmentDate" className="field mt-1" />
              <ErrorMessage name="appointmentDate" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <label className="text-sm font-medium">
              Time
              <Field as="select" name="appointmentTime" className="field mt-1">
                <option value="">Select slot</option>
                {TIME_SLOTS.map((slot) => (
                  <option key={slot} value={slot}>
                    {slot}
                  </option>
                ))}
              </Field>
              <ErrorMessage name="appointmentTime" component="p" className="text-red-600 text-xs mt-1" />
            </label>
            <div className="md:col-span-2 flex gap-3">
              <button className="btn-primary" type="submit" disabled={isSubmitting}>
                {editing ? 'Save changes' : 'Register appointment'}
              </button>
              <button type="button" className="btn-ghost" onClick={() => navigate(-1)}>
                Cancel
              </button>
            </div>
          </Form>
        )}
      </Formik>
    </div>
  );
}
