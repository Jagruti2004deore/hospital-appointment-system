import { useNavigate } from "react-router-dom";

const Home = () => {
  const navigate = useNavigate();

  return (
    <div
      className="min-h-screen bg-cover bg-center"
      style={{ backgroundImage: "url('/hero.png')" }}
    >
      <div className="min-h-screen bg-black/60 flex items-center justify-center">
        <div className="text-center text-white px-6">
          <h1 className="text-5xl font-bold mb-6">
            Hospital Appointment System
          </h1>

          <p className="text-lg mb-8 text-gray-200">
            Book appointments easily. Skip long queues.
          </p>

          <button
            onClick={() => navigate("/login")}
            className="bg-blue-600 hover:bg-blue-700 px-8 py-3 rounded-lg text-lg font-semibold"
          >
            Book Your Appointment
          </button>
        </div>
      </div>
    </div>
  );
};

export default Home;
