

const AppointmentCard = ({
  appointment,
  onCancel,
  onConfirm,
  isAdmin = false,
}) => {
  return (
    <div style={{ border: "1px solid gray", padding: 10, margin: 10 }}>
      <p><b>Name:</b> {appointment.patientName}</p>
      <p><b>Type:</b> {appointment.appointmentType}</p>
      <p><b>Status:</b> {appointment.status}</p>
      <p><b>Date:</b> {appointment.appointmentDate}</p>
      

      {appointment.doctorMessage && (
        <p><b>Doctor Message:</b> {appointment.doctorMessage}</p>
      )}

      {!isAdmin && appointment.status === "WAITING" && (
        <button onClick={() => onCancel(appointment.id)}>Cancel</button>
      )}

      {isAdmin && (
        <>
          <button onClick={() => onConfirm(appointment.id)}>Confirm</button>
          <button onClick={() => onCancel(appointment.id)}>Cancel</button>
        </>
      )}
    </div>
  );
};

export default AppointmentCard;
