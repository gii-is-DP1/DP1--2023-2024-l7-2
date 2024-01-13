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
        username: user1.username,
        password: user1.password
      };

    const [passw, setPassw] = useState("");
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [user, setUser] = useFetchState(
        emptyItem,
        `/api/v1/users/${user1.id}`,
        jwt,
        setMessage,
        setVisible
    );
      
    function handleSubmit(event) {
        event.preventDefault();
    
        if (passw !== ""){
          setUser({ ...user, password: passw })
        }

        fetch("/api/v1/auth/update/" + user.id, {
          method: "POST",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
          body: JSON.stringify(user),
        })
          .then((response) => {
            if (response.status !== 200) return Promise.reject("Invalid login attempt");
            fetch("/api/v1/auth/signin", {
              method: "POST",
              headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
              },
              body: JSON.stringify({username:user.username,password:user.password}),
            }).then((response)=> response.json()).then((data)=> {
              tokenService.setUser(data);
              tokenService.updateLocalAccessToken(data.token);
              //window.location.href = "/user"
            })
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
              name="password"
              id="password"
              value={passw || ""}
              onChange={(e) => setPassw(e.target.value)}
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
