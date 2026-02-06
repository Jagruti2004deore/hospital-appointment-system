import { useState } from "react";
import { useNavigate } from "react-router-dom";
import privateApi from "../api/privateApi";

function AppointmentForm() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    patientName: "",
    age: "",
    bloodGroup: "",
    gender: "",
    contactNumber: "",
    appointmentType: "NEW",
    appointmentDate: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;

    // 📞 Contact number: digits only, max 10
    if (name === "contactNumber") {
      if (!/^\d*$/.test(value)) return;
      if (value.length > 10) return;
    }

    setForm({ ...form, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await privateApi.post("/appointments/book", form);
      alert("Appointment booked successfully");
      navigate("/patient/dashboard");
    } catch (err) {
      alert("Failed to book appointment");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center px-4">
      <div className="bg-white shadow-lg rounded-lg w-full max-w-3xl p-8">
        <h2 className="text-2xl font-bold text-center mb-6 text-blue-600">
          Book Appointment
        </h2>

        {/* 🔹 VIEW HISTORY BUTTON */}
        <div className="text-center mb-6">
          <button
            onClick={() => navigate("/patient/dashboard")}
            className="text-blue-600 underline hover:text-blue-800"
          >
            View My Appointment History
          </button>
        </div>

        <form
          onSubmit={handleSubmit}
          className="grid grid-cols-1 md:grid-cols-2 gap-4"
        >
          {/* Name */}
          <input
            type="text"
            name="patientName"
            placeholder="Patient Name"
            value={form.patientName}
            onChange={handleChange}
            required
            className="border p-2 rounded"
          />

          {/* Age */}
          <input
            type="number"
            name="age"
            placeholder="Age"
            value={form.age}
            onChange={handleChange}
            required
            className="border p-2 rounded"
          />

          {/* Blood Group */}
          <select
            name="bloodGroup"
            value={form.bloodGroup}
            onChange={handleChange}
            required
            className="border p-2 rounded"
          >
            <option value="">Select Blood Group</option>
            <option>A+</option>
            <option>A-</option>
            <option>B+</option>
            <option>B-</option>
            <option>AB+</option>
            <option>AB-</option>
            <option>O+</option>
            <option>O-</option>
          </select>

          {/* Gender */}
          <select
            name="gender"
            value={form.gender}
            onChange={handleChange}
            required
            className="border p-2 rounded"
          >
            <option value="">Select Gender</option>
            <option>Male</option>
            <option>Female</option>
            <option>Other</option>
          </select>

          {/* Contact Number */}
          <input
            type="text"
            name="contactNumber"
            placeholder="Contact Number"
            value={form.contactNumber}
            onChange={handleChange}
            maxLength={10}
            required
            className="border p-2 rounded"
          />

          {/* Appointment Date */}
          <input
            type="date"
            name="appointmentDate"
            value={form.appointmentDate}
            onChange={handleChange}
            required
            className="border p-2 rounded"
          />

          {/* Appointment Type */}
          <select
            name="appointmentType"
            value={form.appointmentType}
            onChange={handleChange}
            className="border p-2 rounded md:col-span-2"
          >
            <option value="NEW">New Appointment</option>
            <option value="FOLLOWUP">Follow-up</option>
          </select>

          {/* Submit */}
          <button
            type="submit"
            className="md:col-span-2 bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
          >
            Submit Appointment
          </button>
        </form>
      </div>
    </div>
  );
}

export default AppointmentForm;
