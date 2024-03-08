import React, { useState } from 'react';
import  '../login_signup_style/style.css';
const Signup=()=>{
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [message, setMessage] = useState("");

  function register(e) {
    e.preventDefault();
    const reqBody = {
      username: username,
      password: password,
      firstName: firstName,
      lastName:lastName
    };
 
    fetch("api/users/register", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "post",
      body: JSON.stringify(reqBody),
    })
      .then((response) => {
        if (response.status === 200){
         
          setMessage("Your account has been registered. Please sign in.");
        }
          
        else {
          setMessage("Register has failed. Please enter another email.");
        }
        setUsername("");
        setFirstName("");
        setLastName("");
        setPassword("");
      })
      .catch((er) => {
        console.log(er);
      });
  }
    
    return (
      <div className="auth-wrapper">
      <div className="auth-inner">
        <form>
        <p style={{'textAlign':'center', 'color':'red', 'marginBottom':"5px"}}> {message} </p>
        <h3>Sign Up</h3>
        <div className="mb-3">
          <label>First name</label>
          <input
            type="text"
            className="form-control"
            placeholder="First name"
            onChange={(e)=>setFirstName(e.target.value)} 
            value={firstName}
          />
        </div>
        <div className="mb-3">
          <label>Last name</label>
          <input type="text" className="form-control" placeholder="Last name"
          onChange={(e)=>setLastName(e.target.value)} 
          value={lastName}
          />
        </div>
        <div className="mb-3">
          <label>Email address</label>
          <input
            type="email"
            className="form-control"
            placeholder="Enter email"
            onChange={(e)=>setUsername(e.target.value)} 
            value={username}
          />
        </div>
        <div className="mb-3">
          <label>Password</label>
          <input
            type="password"
            className="form-control"
            placeholder="Enter password"
            onChange={(e)=>setPassword(e.target.value)} 
            value={password}
          />
        </div>
        <div className="d-grid">
          <button type="submit" className="btn btn-primary" onClick={(e)=>register(e)}>
            Sign Up
          </button>
        </div>
        <p className="forgot-password text-right">
          Already registered? <a href="/signin">sign in</a>
        </p>
      </form>
       </div>
       </div> 
    )
}

export default Signup; 