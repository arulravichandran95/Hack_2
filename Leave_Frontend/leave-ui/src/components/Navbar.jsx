import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { LogOut, Home, PieChart, Users, Menu, X } from 'lucide-react';
import './Navbar.css';

const Navbar = () => {
    const { user, logout } = useAuth();
    const location = useLocation();
    const [isOpen, setIsOpen] = React.useState(false);

    const isAdmin = user?.role === 'ADMIN' || user?.role === 'MANAGER';

    const isActive = (path) => location.pathname === path;

    return (
        <nav className="navbar glass-card">
            <div className="nav-container">
                <Link to="/" className="nav-logo">
                    <span className="logo-icon">L</span>
                    <span>LeavePortal</span>
                </Link>

                <div className={`nav-links ${isOpen ? 'open' : ''}`}>
                    <Link 
                        to="/dashboard" 
                        className={`nav-item ${isActive('/dashboard') ? 'active' : ''}`}
                        onClick={() => setIsOpen(false)}
                    >
                        <Home size={18} />
                        <span>Dashboard</span>
                    </Link>

                    {isAdmin && (
                        <Link 
                            to="/admin" 
                            className={`nav-item ${isActive('/admin') ? 'active' : ''}`}
                            onClick={() => setIsOpen(false)}
                        >
                            <Users size={18} />
                            <span>Admin Panel</span>
                        </Link>
                    )}

                    <div className="nav-divider"></div>

                    <div className="user-profile">
                        <span className="user-name">{user?.firstName}</span>
                        <span className="user-role">{user?.role}</span>
                    </div>

                    <button className="logout-btn" onClick={logout}>
                        <LogOut size={18} />
                        <span>Logout</span>
                    </button>
                </div>

                <div className="mobile-toggle" onClick={() => setIsOpen(!isOpen)}>
                    {isOpen ? <X size={24} /> : <Menu size={24} />}
                </div>
            </div>
        </nav>
    );
};

export default Navbar;
