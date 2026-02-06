import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import privateApi from "../api/privateApi";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
} from "recharts";

function AdminDashboard() {
  const [appointments, setAppointments] = useState([]);
  const [activeAppt, setActiveAppt] = useState(null);
  const [doctorMessage, setDoctorMessage] = useState("");
  const [dateTime, setDateTime] = useState("");

  const navigate = useNavigate();

  useEffect(() => {
    fetchAppointments();
  }, []);

  const fetchAppointments = async () => {
    const res = await privateApi.get("/appointments/admin/all");

    // WAITING first
    const sorted = res.data.sort((a, b) => {
      if (a.status === "WAITING") return -1;
      if (b.status === "WAITING") return 1;
      return 0;
    });

    setAppointments(sorted);
  };

  // 🔴 LOGOUT
  const handleLogout = () => {
    localStorage.clear();
    navigate("/admin/login");
  };

  // 🔢 COUNTS
  const totalCount = appointments.length;
  const waitingCount = appointments.filter(a => a.status === "WAITING").length;
  const confirmedCount = appointments.filter(a => a.status === "CONFIRMED").length;
  const cancelledCount = appointments.filter(a => a.status === "CANCELLED").length;

  // 📊 PIE DATA
  const pieData = [
    { name: "Waiting", value: waitingCount },
    { name: "Confirmed", value: confirmedCount },
    { name: "Cancelled", value: cancelledCount },
  ];

  const COLORS = ["#facc15", "#22c55e", "#ef4444"];

  const confirmAppointment = async () => {
    if (!doctorMessage || !dateTime) {
      alert("Doctor message & date-time required");
      return;
    }

    await privateApi.post(
      `/appointments/admin/${activeAppt.id}/confirm`,
      {
        doctorMessage,
        appointmentDateTime: dateTime,
      }
    );

    updateStatus(activeAppt.id, "CONFIRMED");
    closePopup();
  };

  const cancelAppointment = async (id) => {
    await privateApi.post(`/appointments/admin/${id}/cancel`);
    updateStatus(id, "CANCELLED");
  };

  const updateStatus = (id, status) => {
    const updated = appointments.map((a) =>
      a.id === id ? { ...a, status } : a
    );
    setAppointments(updated);
  };

  const closePopup = () => {
    setActiveAppt(null);
    setDoctorMessage("");
    setDateTime("");
  };

  return (
    <div className="relative min-h-screen overflow-hidden">

      {/* 🔹 BLURRED BACKGROUND */}
      <div
        className="absolute inset-0 bg-cover bg-center blur-md scale-110"
        style={{ backgroundImage: "url('/admindashboard.jpg')" }}
      ></div>

      {/* 🔹 CONTENT */}
      <div className="relative z-10 p-6 bg-gray-100/80 min-h-screen">

        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-3xl font-bold text-blue-600">
            Admin Dashboard
          </h2>

          <button
            onClick={handleLogout}
            className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded shadow"
          >
            Logout
          </button>
        </div>

        {/* COUNTS */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white rounded shadow p-5 text-center">
            <p className="text-gray-500">Total</p>
            <p className="text-3xl font-bold text-blue-600">{totalCount}</p>
          </div>

          <div className="bg-white rounded shadow p-5 text-center">
            <p className="text-gray-500">Waiting</p>
            <p className="text-3xl font-bold text-yellow-500">{waitingCount}</p>
          </div>

          <div className="bg-white rounded shadow p-5 text-center">
            <p className="text-gray-500">Confirmed</p>
            <p className="text-3xl font-bold text-green-600">{confirmedCount}</p>
          </div>

          <div className="bg-white rounded shadow p-5 text-center">
            <p className="text-gray-500">Cancelled</p>
            <p className="text-3xl font-bold text-red-600">{cancelledCount}</p>
          </div>
        </div>

        {/* PIE + LISTS */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-10">

          {/* PIE */}
          <div className="bg-white p-6 rounded shadow flex justify-center">
            <div>
              <h3 className="text-lg font-semibold mb-4 text-center">
                Appointment Status
              </h3>

              <PieChart width={260} height={260}>
                <Pie
                  data={pieData}
                  cx="50%"
                  cy="50%"
                  outerRadius={90}
                  dataKey="value"
                  label
                >
                  {pieData.map((_, index) => (
                    <Cell key={index} fill={COLORS[index]} />
                  ))}
                </Pie>
                <Tooltip />
                <Legend />
              </PieChart>
            </div>
          </div>

          {/* NEW PATIENTS */}
          <div className="bg-white p-6 rounded shadow">
            <h3 className="text-lg font-semibold mb-4">New Patients</h3>
            {appointments.slice(0, 4).map(a => (
              <div key={a.id} className="mb-3 border-b pb-2">
                <p className="font-medium">{a.patientName}</p>
                <p className="text-sm text-gray-500">
                  {a.appointmentDate} • {a.status}
                </p>
              </div>
            ))}
          </div>

          {/* FOLLOW-UP */}
          <div className="bg-white p-6 rounded shadow max-h-[260px] overflow-y-auto">
            <h3 className="text-lg font-semibold mb-4">
              Follow-up Patients
            </h3>
            {appointments.map(a => (
              <div key={a.id} className="mb-3 border-b pb-2">
                <p className="font-medium">{a.patientName}</p>
                <p className="text-sm text-gray-500">
                  {a.appointmentDate} • {a.status}
                </p>
              </div>
            ))}
          </div>
        </div>

        {/* APPOINTMENT CARDS */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {appointments.map((a) => {
            const disabled = a.status !== "WAITING";

            return (
              <div key={a.id} className="bg-white p-5 rounded shadow">
                <p><b>Patient:</b> {a.patientName}</p>
                <p><b>Blood Group:</b> {a.bloodGroup}</p>
                <p><b>Date:</b> {a.appointmentDate}</p>
                <p><b>Queue:</b> {a.queueNumber}</p>

                <p className="mt-2">
                  <b>Status:</b>{" "}
                  <span className={`px-2 py-1 rounded text-sm ${
                    a.status === "WAITING"
                      ? "bg-yellow-100"
                      : a.status === "CONFIRMED"
                      ? "bg-green-200"
                      : "bg-red-200"
                  }`}>
                    {a.status}
                  </span>
                </p>

                <div className="flex gap-2 mt-4">
                  <button
                    disabled={disabled}
                    onClick={() => setActiveAppt(a)}
                    className={`px-3 py-1 rounded text-white ${
                      disabled
                        ? "bg-gray-400 cursor-not-allowed"
                        : "bg-green-600 hover:bg-green-700"
                    }`}
                  >
                    Done
                  </button>

                  <button
                    disabled={disabled}
                    onClick={() => cancelAppointment(a.id)}
                    className={`px-3 py-1 rounded text-white ${
                      disabled
                        ? "bg-gray-400 cursor-not-allowed"
                        : "bg-red-600 hover:bg-red-700"
                    }`}
                  >
                    Cancel
                  </button>
                </div>
              </div>
            );
          })}
        </div>

        {/* POPUP */}
        {activeAppt && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div className="bg-white p-6 rounded w-96">
              <h3 className="text-lg font-bold mb-3">
                Confirm Appointment
              </h3>

              <textarea
                placeholder="Doctor message"
                className="w-full border p-2 rounded mb-3"
                value={doctorMessage}
                onChange={(e) => setDoctorMessage(e.target.value)}
              />

              <input
                type="datetime-local"
                className="w-full border p-2 rounded mb-4"
                value={dateTime}
                onChange={(e) => setDateTime(e.target.value)}
              />

              <div className="flex justify-end gap-2">
                <button
                  onClick={closePopup}
                  className="px-3 py-1 bg-gray-400 text-white rounded"
                >
                  Cancel
                </button>

                <button
                  onClick={confirmAppointment}
                  className="px-3 py-1 bg-blue-600 text-white rounded"
                >
                  Confirm
                </button>
              </div>
            </div>
          </div>
        )}

      </div>
    </div>
  );
}

export default AdminDashboard;
