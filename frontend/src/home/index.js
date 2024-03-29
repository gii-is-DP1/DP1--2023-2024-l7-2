import React, { useState, useEffect } from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import useFetchState from "../util/useFetchState";
import { Button, Table} from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from '../services/token.service.js';
import jwt_decode from "jwt-decode";

const jwt = tokenService.getLocalAccessToken();

export default function Home(){

    const [role, setRole] = useState([]);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [users, setUsers] = useFetchState(
        [],
        `/api/v1/users/${tokenService.getUserName()}/loggedIn`,
        jwt,
        setMessage,
        setVisible
    );

    useEffect(() => {
        if (jwt) {
            setRole(jwt_decode(jwt).authorities[0]);
        }
    }, [jwt])


    const userList = users.map((user) => {
        return (
        <tr key={user.id}>
            <br></br>
            <td>{user.username}</td>
            <br></br>
        </tr>
        );
    });
    let userLinks = <></>;
    let publicLinks = <></>;

    if (!jwt) {
        publicLinks = (
            <>
                <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                        <Link to={"/login"} className="btn" style={{ textDecoration: "none", color: "white"}}>Play Now!</Link>
                </Button>
            </>
        )
    } else {
        userLinks = (
                <>
                    <Button outline color="warning" size="lg">
                        <Link to={`/game/`} className="btn" style={{ textDecoration: "none" }}>Play Now!</Link>
                    </Button>
                </>
            )
    }

    return(
        <div className="home-page-container">
                    <div className="row-6">
                        <div className="hero-div" style={{ marginTop: "150px" }}>
                            {publicLinks}
                            {userLinks}
                        </div>
                    </div>
                    {jwt?<div>
                        <h4 className="friendListTitle">Online users</h4>
                        <br></br>
                        <div className="friendList">{userList}</div>
                    </div>
                    :<br></br>}
        </div>
    );
}