import React from 'react';
import '../styles/center.css'; 
import { Link } from 'react-router-dom'; 

const nicknameStyle = { 
    color: '#444', 
    fontFamily: 'Poppins',
    fontSize: '1.875rem',
    fontStyle: 'normal',
    fontWeight: '500',
    lineHeight: '1.875rem'
}

const buttonStyle = { 
    width: '18.75rem',
    height: '3.75rem',
    flexShrink: '0', 
    borderRadius: '1.875rem',
    background: '#06F',
    color: '#FFF',
    fontFamily: 'Poppins',
    fontSize: '1.125rem',
    fontStyle: 'normal',
    fontWeight: '500',
    lineHeight: '1rem', 
    marginTop:'2.5rem'
}

const bgStyle = { 
  width: '18.75rem',
  height: '18.75rem',
  flexShrink: '0',
  background: '#D9D9D9', 
  marginTop:'2.5rem'
}

const CreateVerifyCode = () => {
  return (
    <div className='center'>
       <p style={nicknameStyle}>Create OTP Code</p>
        <div style={bgStyle}/> {/* QR코드 나타는 곳 */}
        <Link to="/verifyOTP">
          <button style={buttonStyle}>verify OTP</button>
        </Link>
    </div>
  );
};

export default CreateVerifyCode;