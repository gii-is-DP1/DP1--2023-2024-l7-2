import React, { useState, useEffect } from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from '../services/token.service.js';
import jwt_decode from "jwt-decode";

export default function Home(){

    const jwt = tokenService.getLocalAccessToken();
    const [role, setRole] = useState([]);

    useEffect(() => {
        if (jwt) {
            setRole(jwt_decode(jwt).authorities[0]);
        }
    }, [jwt])


    let userLinks = <></>;
    let publicLinks = <></>;

    if (!jwt) {
        publicLinks = (
            <>
                <Button className="btn btn-dark btn-lg" outline color="warning" size="lg">
                        <Link to={"/login"} className="btn" style={{ textDecoration: "none", color: "white"}}>Play Now</Link>
                </Button>
            </>
        )
    } else {
        userLinks = (
                <>
                    <Button outline color="warning" size="lg">
                        <Link to={`/game`} className="btn" style={{ textDecoration: "none" }}>Play Now</Link>
                    </Button>
                </>
            )
    }

    return(
        <div className="home-page-container">
            <div className="hero-div" style={{marginTop: "150px"}}>
                    {publicLinks}
                    {userLinks}
            </div>
        </div>
    );
}