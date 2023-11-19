import {useState} from "react";
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import getErrorModal from "./../util/getErrorModal";
import getIdFromUrl from "./../util/getIdFromUrl";
import useFetchState from "./../util/useFetchState";

const jwt = tokenService.getLocalAccessToken();

export default function AchievementEdit() {
    const id = getIdFromUrl(2);
    const emptyAchievement = {
        id: id==="new"?null:id,
        name: "",
        description: "",
        badgeImage: "",
        threshold: 1,
        actualDescription: ""
    };
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [achievement, setAchievement] = useFetchState(
        emptyAchievement,
        `/api/v1/achievements/${id}`,
        jwt,
        setMessage,
        setVisible,
        id
    );

    const modal = getErrorModal(setVisible, visible, message);

    function handleSubmit(event) {

        event.preventDefault();

        fetch(
            "/api/v1/achievements" + (achievement.id ? "/" + achievement.id : ""),
            {
                method: achievement.id ? "PUT" : "POST",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(achievement),
            }
        )
        .then((response) => response.text())
        .then((data) => {
            if(data==="")
                window.location.href = "/achievements";
            else{
                let json = JSON.parse(data);
                if(json.message){
                    setMessage(JSON.parse(data).message);
                    setVisible(true);
                }else
                    window.location.href = "/achievements";
            }
        })

        .catch((message) => alert(message));
    }


    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setAchievement({ ...achievement, [name]: value });
    }


    return (
        <div className="auth-page-container" style={{marginTop: "65px", height: "100vh"}}>
            <h2 className="text-center">
                {achievement.id ? "Edit Achievement" : "Add Achievement"}
            </h2>
            <div className="auth-form-container">
                {modal}
                <Form onSubmit={handleSubmit}>
                    <div  className="custom-form-input">
                        <Label for="name" className="custom-form-input-label"> Name </Label>
                        <Input
                            type="text"
                            required
                            name="name"
                            id="name"
                            value={achievement.name || ""}
                            onChange={handleChange}
                            className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="description" className="custom-form-input-label"> Description</Label>
                        <Input
                            type="text"
                            required
                            name="description"
                            id="descripction"
                            value={achievement.description || ""}
                            onChange={handleChange}
                            className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="badgeImage" className="custom-form-input-label"> Badge Image Url: </Label>
                        <Input type="text"
                            name="badgeImage"
                            id="badgeImage"
                            value={achievement.badgeImage || ""}
                            onChange={handleChange}
                            className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalIron" className="custom-form-input-label"> Total Iron needed: </Label>
                        <Input
                        type="number"
                        required
                        name="totalIron"
                        id="totalIron"
                        value={achievement.totalIron || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalGold" className="custom-form-input-label"> Total Gold needed: </Label>
                        <Input
                        type="number"
                        required
                        name="totalGold"
                        id="totalGold"
                        value={achievement.totalGold || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalSteal" className="custom-form-input-label"> Total Steal needed: </Label>
                        <Input
                        type="number"
                        required
                        name="totalSteal"
                        id="totalSteal"
                        value={achievement.totalSteal || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalObjects" className="custom-form-input-label"> Total Objects needed: </Label>
                        <Input
                        type="number"
                        required
                        name="totalObjects"
                        id="totalObjects"
                        value={achievement.totalObjects || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalMatches" className="custom-form-input-label"> Total Matches played: </Label>
                        <Input
                        type="number"
                        required
                        name="totalMatches"
                        id="totalMatches"
                        value={achievement.totalMatches || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-form-input">
                        <Label for="totalVictories" className="custom-form-input-label"> Total Matches winned: </Label>
                        <Input
                        type="number"
                        required
                        name="totalVictories"
                        id="totalVictories"
                        value={achievement.totalVictories || ""}
                        onChange={handleChange}
                        className="custom-input"
                        />
                    </div>
                    <div className="custom-button-row">
                        <button className="auth-button">Save</button>
                        <Link to={`/achievements`} className="auth-button" style={{ textDecoration: "none" }}> Cancel </Link>
                    </div>
                </Form>
            </div>
        </div>
    );
}