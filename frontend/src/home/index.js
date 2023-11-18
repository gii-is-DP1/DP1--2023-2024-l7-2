import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from '../services/token.service.js';

export default function Home(){

    const jwt = tokenService.getLocalAccessToken();

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