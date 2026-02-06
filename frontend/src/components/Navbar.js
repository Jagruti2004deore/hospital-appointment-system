import { Link, useNavigate, useLocation } from "react-router-dom";

const Navbar = () => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const navigate = useNavigate();
  const location = useLocation();

  const isHome = location.pathname === "/";

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <nav className="w-full bg-blue-700 text-white px-6 py-4 flex justify-between items-center">
      <Link to="/" className="text-xl font-bold">
        🏥 Hospital
      </Link>

      <div className="flex gap-4 items-center">

        {/* ✅ HOME PAGE → ALWAYS PUBLIC */}
        {isHome && (
          <>
            <Link to="/login">Patient Login</Link>
            <Link to="/register">Register</Link>
            <Link
              to="/admin/login"
              className="bg-white text-blue-700 px-3 py-1 rounded"
            >
              Admin Login
            </Link>
          </>
        )}

        {/* 👤 PATIENT */}
        {!isHome && token && role === "ROLE_USER" && (
          <>
            <Link to="/appointments/book">Book Appointment</Link>
            <Link to="/patient/dashboard">Dashboard</Link>
            <button onClick={handleLogout} className="bg-red-500 px-3 py-1 rounded">
              Logout
            </button>
          </>
        )}

        {/* 🧑‍⚕️ ADMIN */}
        {!isHome && token && role === "ROLE_ADMIN" && (
          <>
            <Link to="/admin/dashboard">Admin Dashboard</Link>
            <button onClick={handleLogout} className="bg-red-500 px-3 py-1 rounded">
              Logout
            </button>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
