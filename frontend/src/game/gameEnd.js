import useFetchState from "../util/useFetchState";
import { useState } from "react";
import { Button } from "reactstrap";
import { Link } from "react-router-dom";
import getIdFromUrl from "./../util/getIdFromUrl";
import tokenService from "../services/token.service";

const jwt = tokenService.getLocalAccessToken();

export default function Home(){

    const code = getIdFromUrl(2);
    console.log(jwt)

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    const [winner] = useFetchState(
        {},
        `/api/v1/game/play/${code}/winner`,
        jwt,
        setMessage,
        setVisible,
        code
    );

    return(
        <div className="home-page-container">
            <div className="hero-div" style={{marginTop: "150px"}}>
                <h1>
                    The game has ended!<br></br>
                    The winner is {winner}
                </h1>
                <div className="hero-div" style={{marginTop: "150px"}}>
                    <Button outline color="warning" size="lg">
                        <Link to={`/`} className="btn" style={{ textDecoration: "none" }}>Main menu</Link>
                    </Button>
                </div>
            </div>
        </div>
    );
}