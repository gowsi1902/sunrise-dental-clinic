export default function Help() {
  return (
    <div className="max-w-3xl space-y-6">
      <header>
        <h1 className="font-display text-3xl text-clinic-900">Help for new staff</h1>
        <p className="text-slate-500 text-sm">Step-by-step use of the Sunrise Dental Clinic system.</p>
      </header>
      <section className="card p-6 space-y-4 text-sm leading-6">
        <h2 className="font-semibold text-clinic-800">1. Login</h2>
        <p>Enter your username and password. Only authorised staff can open the workspace.</p>
        <h2 className="font-semibold text-clinic-800">2. Register a new appointment</h2>
        <ol className="list-decimal ml-5 space-y-1">
          <li>Choose Register visit.</li>
          <li>Enter patient name, address and contact number.</li>
          <li>Select dentist, treatment type, date and a 30-minute slot.</li>
          <li>The system issues an appointment number such as SDC-0001.</li>
          <li>The same dentist cannot be booked twice in the same slot.</li>
        </ol>
        <h2 className="font-semibold text-clinic-800">3. Display appointment details</h2>
        <p>
          On the home page, type the appointment number and click Display details, or open a row from
          the list.
        </p>
        <h2 className="font-semibold text-clinic-800">4. Calculate and print the bill</h2>
        <p>
          Open Calculate / print bill. Total = treatment fee + consultation fee. Print the receipt, then
          collect payment if the balance is outstanding.
        </p>
        <h2 className="font-semibold text-clinic-800">5. Help section</h2>
        <p>
          Open Help from the left menu whenever you need these steps. There is no paper notebook for
          training new reception staff.
        </p>
        <h2 className="font-semibold text-clinic-800">6. Exit system</h2>
        <p>Use Exit system in the left menu. This signs you out safely and ends the session.</p>
      </section>
    </div>
  );
}
