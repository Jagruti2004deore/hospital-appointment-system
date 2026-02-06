import { useEffect, useState } from "react";
import privateApi from "../api/privateApi";

function PatientDashboard() {
  const [appointments, setAppointments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    privateApi
      .get("/appointments/my")
      .then((res) => {
        setAppointments(res.data);
        setLoading(false);
      })
      .catch(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <p className="text-lg text-gray-600">Loading appointments...</p>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h2 className="text-3xl font-bold text-center text-blue-600 mb-8">
        My Appointments
      </h2>

      {appointments.length === 0 ? (
        <div className="text-center text-gray-600">
          <p>No appointments found.</p>
        </div>
      ) : (
        <div className="max-w-4xl mx-auto grid gap-6">
          {appointments.map((a) => (
            <div
              key={a.id}
              className="bg-white rounded-lg shadow-md p-6 border-l-4 border-blue-500"
            >
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <p>
                  <span className="font-semibold">Patient:</span>{" "}
                  {a.patientName}
                </p>

                <p>
                  <span className="font-semibold">Appointment Type:</span>{" "}
                  {a.appointmentType}
                </p>

                <p>
                  <span className="font-semibold">Status:</span>{" "}
                  <span
                    className={`px-2 py-1 rounded text-white text-sm ${
                      a.status === "PENDING"
                        ? "bg-yellow-500"
                        : a.status === "COMPLETED"
                        ? "bg-green-600"
                        : "bg-blue-500"
                    }`}
                  >
                    {a.status}
                  </span>
                </p>

                <p>
                  <span className="font-semibold">Queue Number:</span>{" "}
                  {a.queueNumber}
                </p>

                <p className="md:col-span-2">
                  <span className="font-semibold">Doctor Message:</span>{" "}
                  {a.doctorMessage || "—"}
                </p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default PatientDashboard;
