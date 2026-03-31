import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../services/api';
import { UserPlus, Mail, Lock, User, Briefcase, Loader2, UserCircle } from 'lucide-react';
import './RegisterPage.css';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        role: 'EMPLOYEE',
        department: ''
    });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            await authService.register(formData);
            navigate('/login', { state: { message: 'Registration successful! Please login.' } });
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    return (
        <div className="register-page">
            <div className="register-container glass-card">
                <div className="register-header">
                    <div className="register-logo">L</div>
                    <h1>Create Account</h1>
                    <p>Join our modern employee leave management portal</p>
                </div>

                <form onSubmit={handleSubmit} className="register-form">
                    {error && <div className="error-alert">{error}</div>}

                    <div className="form-row">
                        <div className="form-group">
                            <label>First Name</label>
                            <div className="input-wrapper">
                                <User className="input-icon" size={18} />
                                <input 
                                    type="text" 
                                    name="firstName"
                                    placeholder="John" 
                                    required 
                                    value={formData.firstName}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>

                        <div className="form-group">
                            <label>Last Name</label>
                            <div className="input-wrapper">
                                <User className="input-icon" size={18} />
                                <input 
                                    type="text" 
                                    name="lastName"
                                    placeholder="Doe" 
                                    required 
                                    value={formData.lastName}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>

                    <div className="form-group">
                        <label>Email Address</label>
                        <div className="input-wrapper">
                            <Mail className="input-icon" size={18} />
                            <input 
                                type="email" 
                                name="email"
                                placeholder="john@company.com" 
                                required 
                                value={formData.email}
                                onChange={handleChange}
                            />
                        </div>
                    </div>

                    <div className="form-group">
                        <label>Password</label>
                        <div className="input-wrapper">
                            <Lock className="input-icon" size={18} />
                            <input 
                                type="password" 
                                name="password"
                                placeholder="Min. 6 characters" 
                                required 
                                minLength={6}
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </div>
                    </div>

                    <div className="form-row">
                        <div className="form-group">
                            <label>Role</label>
                            <div className="input-wrapper">
                                <UserCircle className="input-icon" size={18} />
                                <select 
                                    name="role"
                                    required 
                                    value={formData.role}
                                    onChange={handleChange}
                                >
                                    <option value="EMPLOYEE">Employee</option>
                                    <option value="MANAGER">Manager</option>
                                    <option value="ADMIN">Admin</option>
                                </select>
                            </div>
                        </div>

                        <div className="form-group">
                            <label>Department</label>
                            <div className="input-wrapper">
                                <Briefcase className="input-icon" size={18} />
                                <input 
                                    type="text" 
                                    name="department"
                                    placeholder="Engineering" 
                                    required 
                                    value={formData.department}
                                    onChange={handleChange}
                                />
                            </div>
                        </div>
                    </div>

                    <button type="submit" className="register-btn" disabled={isLoading}>
                        {isLoading ? (
                            <Loader2 className="animate-spin" size={20} />
                        ) : (
                            <>
                                <UserPlus size={20} />
                                <span>Create Account</span>
                            </>
                        )}
                    </button>
                </form>

                <div className="register-footer">
                    <p>Already have an account? <Link to="/login">Sign in</Link></p>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
