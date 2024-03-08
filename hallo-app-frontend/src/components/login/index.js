import React, { useEffect, useState } from 'react';
import style from '../login_signup_style/style.css';
import '../util/useLocalStorage';
import { useLocalState } from '../util/useLocalStorage';
const Login=()=>{
    const[email, setEmail]=useState("");
    const[password, setPassword]=useState("");
    const[jwt, setJwt]=useLocalState("", "jwt");
    const[errorMessage, setErrorMessage]=useState("");


    function sendLoginRequest(){
  
         
            const requestBody={
            "username":email, 
            "password":password
            };
            fetch('api/auth/login', {
            headers:{"Content-Type":"application/json"},
            method: "post",
            body: JSON.stringify(requestBody)
            }).then(response=>{
            console.log(response.status)
            if(response.status===200){
                return response.headers.get('Authorization')
            }
            else{setErrorMessage("Email/Password is incorrrect. Please try again.");return Promise.reject("Invalid Credentials")}
        }) .then(
            response=> {
                
                setJwt(response);
                //Remember. This is an automated redirect
                window.location.href="home";


            }
            
            
        ).catch(e=>console.log(e))
      }


    return (
        <div className="auth-wrapper">
        <div className="auth-inner">
        <form>

           <p style={{'textAlign':'center', 'color':'red'}}> {errorMessage} </p>
            <h3>Sign In</h3>
            <div className="mb-3">
            <label>Email address</label>
            <input
                type="email"
                className="form-control"
                onChange={(e)=>setEmail(e.target.value)} 
                    value={email}
                placeholder="Enter email"
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
            <div className="mb-3">
            {/* <div className="custom-control custom-checkbox">
                <input
                type="checkbox"
                className="custom-control-input"
                id="customCheck1"
                />
                <label className="custom-control-label" htmlFor="customCheck1">
                Remember me
                </label>
            </div> */}
            </div>
            <div className="d-grid">
            <button type="button" className="btn btn-primary" onClick={()=>sendLoginRequest()}>
                Submit

            </button>
            </div>
            {/* <p className="forgot-password text-right">
            Forgot <a href="#">password?</a>
            </p> */}

            <p className={style.forgot_password} >
            If not already registered, please  <a href="signup">sign up</a>.
            </p>
      </form>
            </div>
            </div>
    )
}

export default Login; 