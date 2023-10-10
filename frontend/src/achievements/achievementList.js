import {
    Table, Button
} from "reactstrap";

import { Link } from "react-router-dom";

import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

// const achievements = [
//     {id:1, name:"Experiencia básica", description:"Si juegas 10 partidas o más", badgeImage:"https://cdn-icons-png.flaticon.com/512/5243/5243423.png", 
//     threshold:"10", metric:"GAMES_PLAYED"},
//     {id:2, name:"Explorador", description:"Si juegas 25 partidas o más", badgeImage:"https://cdn-icons-png.flaticon.com/512/603/603855.png", 
//     threshold:"25", metric:"GAMES_PLAYED"},
//     {id:3, name:"Experto", description:"Si ganas 20 partidas o más", badgeImage:"https://cdn-icons-png.flaticon.com/512/4737/4737471.png", 
//     threshold:"20", metric:"VICTORIES"}
// ];

export default function AchievementList() {
    const [achievements, setAchievements] = useFetchState(
        [],
        `/api/v1/achievements`,
        jwt
    );

    function deleteAchievement(url,id,achievements,setAchievement) {
        console.log(url)
        let confirmMessage = window.confirm("Are you sure you want to delete it?");
        if (confirmMessage) {
            fetch(url, {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                },
            })
                .then((response) => {
                    setAchievement(achievements.filter((i) => i.id !== id))
                    return response.text();
                })
                .catch((err) => {
                    console.log(err);
                    alert("Error deleting entity")
                });
        }
    }

    const achievementList = achievements.map((a) => {
        return (
            <tr key={a.id}>
                <td className="text-center">{a.name}</td>
                <td className="text-center"> {a.description} </td>
                <td className="text-center">
                    <img src={a.badgeImage ? a.badgeImage : imgnotfound} alt={a.name} width="50px" />
                </td>
                {a.totalIron !== 0 ? (<td className="text-center">{a.totalIron}</td>) : null}
                {a.totalGold !== 0 ? (<td className="text-center">{a.totalGold}</td>) : null}
                {a.totalSteal !== 0 ? (<td className="text-center">{a.totalSteal}</td>) : null}
                {a.totalObjects !== 0 ? (<td className="text-center">{a.totalObjects}</td>) : null}
                {a.totalMatches !== 0 ? (<td className="text-center">{a.totalMatches}</td>) : null}
                {a.totalVictories !== 0 ? (<td className="text-center">{a.totalVictories}</td>) : null}
                <Button outline color="success" >
                    <Link to={`/achievements/${a.id}`} className="btn sm" style={{ textDecoration: "none" }}>Edit</Link>
                </Button>
                <Button outline color="danger" >
                    <Link onClickCapture={() => deleteAchievement(`/api/v1/achievements/${a.id}`,a.id, achievements,setAchievements)} 
                    className="btn sm" style={{ textDecoration: "none" }}>Delete</Link>
                </Button>
            </tr>
        );
    });
    return (
        <div>
            <div className="admin-page-container">
                <h1 className="text-center">Achievements</h1>
                <div>
                    <Table aria-label=
                        "achievements" className="mt-4">
                        <thead>
                            <tr>
                                <th className="text-center">Name</th>
                                <th className="text-center">Description</th>
                                <th className="text-center">Image</th>
                                {achievementList.some((a) => a.props.totalIron !== 0) ? (
                                    <th className="text-center">Total Iron</th>) : null
                                }
                                {achievementList.some((a) => a.props.totalGold !== 0) ? (
                                    <th className="text-center">Total Gold</th>) : null
                                }
                                {achievementList.some((a) => a.props.totalSteal !== 0) ? (
                                    <th className="text-center">Total Steal</th>) : null
                                }
                                {achievementList.some((a) => a.props.totalObjects !== 0) ? (
                                    <th className="text-center">Total Objects</th>) : null
                                }
                                {achievementList.some((a) => a.props.totalMatches !== 0) ? (
                                    <th className="text-center">Total Matches</th>) : null
                                }
                                {achievementList.some((a) => a.props.totalVictories !== 0) ? (
                                    <th className="text-center">Total Victories</th>) : null
                                }

                            </tr>
                        </thead>
                        <tbody>{achievementList}</tbody>
                    </Table>
                    <Button outline color="success" >
                        <Link to={`/achievements/new`} className="btn sm" style={{ textDecoration: "none" }}>Create achievement</Link>
                    </Button>
                </div>
            </div>
        </div>
    );
}