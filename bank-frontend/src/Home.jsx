import React from "react";

function Home({ setPage }) {
  const heroImageUrl = "https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80";

  return (
    <div className="full-height-center">
      <img src={heroImageUrl} alt="Bank Hero" className="hero-bg" />
      
      <div className="glass-panel" style={{ textAlign: "center" }}>
        <div style={{ display: 'flex', justifyContent: 'center', marginBottom: '30px' }}>
            <div style={{ padding: '20px', background: 'rgba(46, 125, 255, 0.1)', borderRadius: '24px', border: '1px solid rgba(46, 125, 255, 0.2)' }}>
                <span style={{ fontSize: '3rem' }}>🏦</span>
            </div>
        </div>
        
        <h1 style={{ 
            fontSize: '3rem', 
            fontWeight: '800', 
            marginBottom: '16px', 
            color: 'white',
            letterSpacing: '-1.5px'
        }}>
            Harish Smart Bank
        </h1>
        
        <p style={{ 
            marginBottom: "48px", 
            color: "var(--text-secondary)", 
            fontSize: '1.1rem',
            lineHeight: '1.6'
        }}>
          Premium financial solutions for the digital age. Secure, intelligent, and designed around you.
        </p>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            <button className="premium-btn btn-cyan" onClick={() => setPage("loginSelection")}>Access My Account</button>
            <button className="premium-btn btn-outline" onClick={() => setPage("create")}>Open New Account</button>
        </div>

        <div style={{ marginTop: '40px', paddingTop: '30px', borderTop: '1px solid var(--glass-border)', display: 'flex', justifyContent: 'space-around' }}>
            <div style={{ textAlign: 'center' }}>
                <div style={{ fontWeight: '800', fontSize: '1.25rem', color: 'var(--bright-blue)' }}>1M+</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '1px' }}>Clients</div>
            </div>
            <div style={{ textAlign: 'center' }}>
                <div style={{ fontWeight: '800', fontSize: '1.25rem', color: 'var(--bright-blue)' }}>$50B+</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '1px' }}>Assets</div>
            </div>
            <div style={{ textAlign: 'center' }}>
                <div style={{ fontWeight: '800', fontSize: '1.25rem', color: 'var(--bright-blue)' }}>99.9%</div>
                <div style={{ fontSize: '0.75rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '1px' }}>Uptime</div>
            </div>
        </div>
      </div>
    </div>
  );
}

export default Home;