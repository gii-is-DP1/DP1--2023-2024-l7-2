import { useState, useRef } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../services/token.service";

import useFetchState from "../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function FormRequest() {
  const [message, setMessage] = useState("");
  const user = tokenService.getUser();
  const [request, setRequest] = useFetchState({
    id: null,
    sender: user,
    receiver: null,
    status: {
      id: 1,
      name: "Sent",
    },
    sendTime: null,
  });

  function searchUser(name) {
    fetch(`/api/v1/users/check/${name}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((user) => {
        // Verifica si se encontró un usuario antes de actualizar el estado
        if (user) {
          setRequest((prevRequest) => ({
            ...prevRequest,
            receiver: user,
          }));
        } else {
          alert("User not found");
        }
      })
      .catch((err) => {
        console.log(err);
        alert("Error searching user: " + err.message);
      });
  }

  function handleSubmit(event) {
    event.preventDefault();
    fetch("/api/v1/friends", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(request),
    })
      .then((response) => response.text())
      .then((newRequest) => {
        console.log("New Request created:", newRequest);
        setMessage("Solicitud enviada")
        window.confirm(message)
      })
      .catch((err) => {
        console.log(err);
        alert("Error creating request: " + err.message);
      });
  }

  console.log("Request Body:", JSON.stringify(request));

  return (
    <div className="auth-page-container" style={{ height: "100vh" }}>
      <h1 className="text-center">{"Send request"}</h1>
      <div className="custom-form-input">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Username to send request:
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={request.receiver ? request.receiver.username : ""}
              onChange={(e) => {
                const username = String(e.target.value);
                searchUser(username);
              }}
              className="custom-input"
            />
          </div>
          <div className="custom-button-row">
            <button className="auth-button" type="submit">
              Save
            </button>
            <Link
              to={`/friends`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Cancel
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}
