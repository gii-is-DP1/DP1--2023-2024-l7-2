import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";
import useFetchData from "../../util/useFetchData";
import useFetchState from "../../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function UserEditAdmin() {
  const emptyItem = {
    id: null,
    username: "",
    password: "",
    authority: null,
  };
  const id = getIdFromUrl(2);
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [user,setUser] = useState({})
  const [falseUser, setFalseUser] = useFetchState(
    emptyItem,
    `/api/v1/users/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );

  useEffect(() => {
    falseUser.password = "";
    setUser(falseUser);
  },[falseUser])


  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value });
  }

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
        } else window.location.href = "/users";
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="auth-page-container" style={{height: "100vh"}}>
      <h1 className="text-center">
        {user.id ? "Edit User" : "Add User"}</h1>
      <div className="custom-form-input">
        {modal}
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
              onChange={handleChange}
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
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-button-row">
            <button className="auth-button">Save</button>
            <Link
              to={`/users`}
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
