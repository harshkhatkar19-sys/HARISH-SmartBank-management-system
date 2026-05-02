import React, { useState } from "react";
import { login } from "../api";

function UserLogin({ setPage }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const heroImageUrl = "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80";

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const data = await login(username.trim(), password.trim());
      if (data && (data.accNo || data.accountNumber || data.id)) {
        localStorage.setItem("user", JSON.stringify(data));
        setPage("dashboard");
      } else {
        setError("Invalid username or password");
      }
    } catch {
      setError("Login failed. Please check your credentials.");
    }
  };

  return (
    <div className="full-height-center">
      <img src={heroImageUrl} alt="Bank Hero" className="hero-bg" />
      
      <div className="glass-panel">
        <h2 className="section-title" style={{ fontSize: '2.5rem', marginBottom: '10px', textAlign: 'center' }}>User Login</h2>
        <p style={{ textAlign: "center", marginBottom: "40px", color: "var(--text-secondary)" }}>Securely access your bank account</p>
        
        {error && <div style={{ marginBottom: '24px', padding: '16px', borderRadius: '12px', background: 'rgba(239, 68, 68, 0.1)', color: 'var(--danger)', textAlign: 'center', fontWeight: '600', border: '1px solid rgba(239, 68, 68, 0.2)' }}>{error}</div>}

        <form onSubmit={handleLogin}>
          <label>Username</label>
          <input 
            type="text" 
            placeholder="e.g. johndoe123" 
            value={username}
            onChange={(e) => setUsername(e.target.value)} 
            required
          />
          
          <label>Password</label>
          <input 
            type="password" 
            placeholder="••••••••" 
            value={password}
            onChange={(e) => setPassword(e.target.value)} 
            required
          />

          <button type="submit" className="premium-btn btn-blue" style={{ marginTop: '10px' }}>
            Sign In Securely
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

export default UserLogin;