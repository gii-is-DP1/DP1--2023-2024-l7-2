import React from 'react';
import '../App.css';
import '../static/css/home/home.css'; 
import logo from '../static/images/logoPNG_3.png';
import { Button } from "reactstrap";
import { Link } from "react-router-dom";

export default function Home(){
    return(
        <div className="home-page-container">
            <div className="hero-div" style={{marginTop: "150px"}}>
                    <Button outline color="warning" size="lg">
                        <Link to={`/game`} className="btn" style={{ textDecoration: "none" }}>Play Now</Link>
                    </Button>
            </div>
        </div>
    );
}