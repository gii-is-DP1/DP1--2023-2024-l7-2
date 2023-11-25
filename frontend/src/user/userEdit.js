import { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../services/token.service";
import getIdFromUrl from "../util/getIdFromUrl";
import useFetchData from "../util/useFetchData";
import useFetchState from "../util/useFetchState";


const jwt = tokenService.getLocalAccessToken();

export default function UserEditPage() {
    const user1 = tokenService.getUser();
    const emptyItem = {
        id: user1.id,
        username: "",
        name:"",
        password: ""
      };

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [user, setUser] = useFetchState(
        emptyItem,
        `/api/v1/users/${user1.id}`,
        jwt,
        setMessage,
        setVisible
    );

        console.log(`/api/v1/users/${user.id}`)
      function handleSubmit(event) {
        event.preventDefault();
    
        fetch("/api/v1/users" + (user.id ? "/" + user.id : ""), {
          method: user.id ? "PUT" : "POST",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify(user),
        })
          .then((response) => response.json())
          .then((json) => {
            if (json.message) {
              setMessage(json.message);
              setVisible(true);
            } else window.location.href = "/user";
          })
          .catch((message) => alert(message));
      }

      return(
        <div className="auth-page-container" style={{height: "100vh"}}>
      <h1 className="text-center">
        {user.id ? "Edit User" : "Add User"}</h1>
      <div className="custom-form-input">
        <Form onSubmit={handleSubmit}>
        <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Name
            </Label>
            <Input
              type="text"
              required
              name="name"
              id="name"
              value={user.name || ""}
              onChange={(e) => setUser({ ...user, name: e.target.value })}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Username
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={(e) => setUser({ ...user, username: e.target.value })}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="lastName" className="custom-form-input-label">
              Password
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={(e) => setUser({ ...user, password: e.target.value })}
              className="custom-input"
            />
          </div>
          <div className="custom-button-row">
            <button className="auth-button">Save</button>
            <Link
              to={`/user`}
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
