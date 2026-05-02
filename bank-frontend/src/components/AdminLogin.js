import React, { useState } from "react";

function AdminLogin({ setPage }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const heroImageUrl = "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80";

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const res = await fetch("http://localhost:8080/api/admin/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
      });

      if (res.ok) {
        setPage("adminDashboard");
      } else {
        setError("Invalid admin credentials");
      }
    } catch (err) {
      setError("Login failed. Check server connection.");
    }
  };

  return (
    <div className="full-height-center">
      <img src={heroImageUrl} alt="Bank Hero" className="hero-bg" />

      <div className="glass-panel">
        <h2 className="section-title" style={{ fontSize: '2.5rem', marginBottom: '10px', textAlign: 'center' }}>Staff Portal</h2>
        <p style={{ textAlign: "center", marginBottom: "40px", color: "var(--text-secondary)" }}>Administrator authorization required</p>
        
        {error && <div style={{ marginBottom: '24px', padding: '16px', borderRadius: '12px', background: 'rgba(239, 68, 68, 0.1)', color: 'var(--danger)', textAlign: 'center', fontWeight: '600', border: '1px solid rgba(239, 68, 68, 0.2)' }}>{error}</div>}

        <form onSubmit={handleLogin}>
          <label>Administrator ID</label>
          <input 
            type="text" 
            placeholder="e.g. admin_01" 
            value={username}
            onChange={(e) => setUsername(e.target.value)} 
            required
          />
          
          <label>Security Key</label>
          <input 
            type="password" 
            placeholder="••••••••" 
            value={password}
            onChange={(e) => setPassword(e.target.value)} 
            required
          />

          <button type="submit" className="premium-btn btn-cyan" style={{ marginTop: '10px' }}>
            Verify & Enter
          </button>
        </form>

        <button 
          className="premium-btn" 
          style={{ marginTop: '20px', background: 'transparent', color: 'var(--text-secondary)', fontWeight: '500' }} 
          onClick={() => setPage("loginSelection")}
        >
          ← Back to Selection
        </button>
      </div>
    </div>
  );
}

export default AdminLogin;