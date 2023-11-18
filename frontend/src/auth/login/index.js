import React, { useState } from "react";
import { Link } from 'react-router-dom';
import { Alert, Button} from "reactstrap";
import FormGenerator from "../../components/formGenerator/formGenerator";
import tokenService from "../../services/token.service";
import "../../static/css/auth/authButton.css";
import { loginFormInputs } from "./form/loginFormInputs";

export default function Login() {
  const [message, setMessage] = useState(null)
  const loginFormRef = React.createRef();      
  

  async function handleSubmit({ values }) {

    const reqBody = values;
    setMessage(null);
    await fetch("/api/v1/auth/signin", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(reqBody),
    })
      .then(function (response) {
        if (response.status === 200) return response.json();
        else return Promise.reject("Invalid login attempt");
      })
      .then(function (data) {
        tokenService.setUser(data);
        tokenService.updateLocalAccessToken(data.token);
        window.location.href = "/";
      })
      .catch((error) => {         
        setMessage(error);
      });            
  }
  
    return (
      <div className="auth-page-container" style={{height: "100vh"}}>
        {message ? (
          <Alert color="primary">{message}</Alert>
        ) : (
          <></>
        )}

        <h1>Login</h1>

        <div  className="custom-form-input">
          <FormGenerator
            ref={loginFormRef}
            inputs={loginFormInputs}
            onSubmit={handleSubmit}
            numberOfColumns={1}
            listenEnterKey
            buttonText="Login"
            buttonClassName="auth-button"
          />
          <div className="col text-center" style={{marginTop: "20px"}}>
              <h4>¿No tienes cuenta? </h4>
              <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                <Link to={"/register"}className="btn" style={{ textDecoration: "none", color: "white"}}>Regístrate aquí</Link>
              </Button>
          </div>
        </div>
      </div>
    );  
}