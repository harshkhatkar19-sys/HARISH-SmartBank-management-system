import React, { useState } from "react";
import Home from "./Home";
import LoginSelection from "./components/LoginSelection";
import UserLogin from "./components/UserLogin";
import AdminLogin from "./components/AdminLogin";
import CreateAccount from "./components/CreateAccount";
import Dashboard from "./components/Dashboard";
import AdminDashboard from "./components/AdminDashboard";

function App() {
  const [page, setPage] = useState("home");

  return (
    <>
      {page === "home" && <Home setPage={setPage} />}
      {page === "loginSelection" && <LoginSelection setPage={setPage} />}
      {page === "userLogin" && <UserLogin setPage={setPage} />}
      {page === "adminLogin" && <AdminLogin setPage={setPage} />}
      {page === "create" && <CreateAccount setPage={setPage} />}
      {page === "dashboard" && <Dashboard setPage={setPage} />}
      {page === "adminDashboard" && <AdminDashboard setPage={setPage} />}
    </>
  );
}

export default App;