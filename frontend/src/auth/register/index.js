import "../../static/css/auth/authButton.css";
import "../../static/css/auth/authPage.css";
import tokenService from "../../services/token.service";
import FormGenerator from "../../components/formGenerator/formGenerator";
import { registerFormOwnerInputs } from "./form/registerFormOwnerInputs";
import { registerFormVetInputs } from "./form/registerFormVetInputs";
import { registerFormClinicOwnerInputs } from "./form/registerFormClinicOwnerInputs";
import { useEffect, useRef, useState } from "react";

export default function Register() {
  const authority = "USER"
  let [clinics, setClinics] = useState([]);

  const registerFormRef = useRef();

  function handleSubmit({ values }) {

    if(!registerFormRef.current.validate()) return;

    console.log(values)
    const request = values;
    //request.clinic = clinics.filter((clinic) => clinic.name === request.clinic)[0];
    request["authority"] = authority;
    let state = "";

    fetch("/api/v1/auth/signup", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      body: JSON.stringify(request),
    })
      .then(function (response) {
        if (response.status === 200) {
          const loginRequest = {
            username: request.username,
            password: request.password,
          };

          fetch("/api/v1/auth/signin", {
            headers: { "Content-Type": "application/json" },
            method: "POST",
            body: JSON.stringify(loginRequest),
          })
            .then(function (response) {
              if (response.status === 200) {
                state = "200";
                return response.json();
              } else {
                state = "";
                return response.json();
              }
            })
            .then(function (data) {
              if (state !== "200") alert(data.message);
              else {
                tokenService.setUser(data);
                tokenService.updateLocalAccessToken(data.token);
                window.location.href = "/";
              }
            })
            .catch((message) => {
              alert(message);
            });
        } else {
          throw new Error('Username is already taken!');
        }
      })
      .catch((message) => {
        alert(message);
      });
  }

  return (
    <div className="auth-page-container"  style={{height: "100vh"}}>
      <h1>Register</h1>
      <div className="custom-form-input">
        <FormGenerator
          ref={registerFormRef}
          inputs={registerFormOwnerInputs}
          onSubmit={handleSubmit}
          numberOfColumns={1}
          listenEnterKey
          buttonText="Save"
          buttonClassName="auth-button"
        />
      </div>
    </div>
  );

}
