import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import './App.css';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import jwt_decode from "jwt-decode";
import Login from './components/login';
import Signup from './components/signup';
import PrivateRoute from './components/private_route';
import ResearcherMain from './components/researcher_main_page';
import AnnotatorMain from './components/annotator';
import { useLocalState } from './components/util/useLocalStorage';
import { useState } from 'react';

function App() {
  const [jwt, setJwt]=useLocalState("", "jwt")
  const[roles, setRoles]=useState(getRoleFromJWT());

  function getRoleFromJWT(){
    if(jwt)  {
      const decodedJwt=jwt_decode(jwt);
      console.log(decodedJwt)
      return decodedJwt.authorities;
    }
        else{
          return [];
        }
  }

  return (
    <Router>
    <div className="App">

    <Routes>
      
      <Route path="/home"
                  element={
                    roles.find((role) => role === "ROLE_RESEARCHER") ?  (
                      <PrivateRoute>
                        <ResearcherMain />
                      </PrivateRoute>
                    ):(
                      <PrivateRoute>
                        <AnnotatorMain/>
                      </PrivateRoute>
                    ) 
                  }
                >
            </Route>  
      </Routes>
    

      <Routes>
              <Route exact path="/" element={<Login />} />
              <Route path="/signin" element={<Login />} />
              <Route path="/signup" element={<Signup />} />

      </Routes>
   
    

     

    </div>
  </Router>
  );
}

export default App;
