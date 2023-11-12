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
                    <h1>
                    The game has ended!
                    </h1>
            </div>
        </div>
    );
}